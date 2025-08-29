#!/bin/bash

if ! [ -x "$(command -v docker-compose)" ]; then
  echo 'Error: docker-compose is not installed.' >&2
  exit 1
fi

domains=(yourdomain.com)
rsa_key_size=4096
data_path="./data/certbot"
email="your-email@example.com" 
staging=0

if [ -d "$data_path" ]; then
  read -p "Existing certificate data found for $domains. Continue and replace existing certificate? (y/N)" decision
  if [ "$decision" != "y" ]; then
    exit
  fi
fi

if [ ! -e "$data_path/options-ssl-nginx.conf" ] || [ ! -e "$data_path/ssl-dhparams.pem" ]; then
  echo "### Downloading recommended TLS parameters ###"
  mkdir -p "$data_path"
  curl -s https://raw.githubusercontent.com/certbot/certbot/master/certbot/nginx/options-ssl-nginx.conf > "$data_path/options-ssl-nginx.conf"
  curl -s https://ssl-config.mozilla.org/ffdhe4096.txt > "$data_path/ssl-dhparams.pem"
  echo
fi

echo "### Creating dummy certificate for $domains ###"
path="/etc/letsencrypt/live/$domains"
mkdir -p "$data_path/conf/live/$domains"
docker-compose run --rm --entrypoint "\
  openssl req -x509 -nodes -newkey rsa:1024 -days 1\
    -keyout '$path/privkey.pem' \
    -out '$path/fullchain.pem' \
    -subj '/CN=localhost'" certbot
echo

echo "### Starting nginx ###"
docker-compose up --force-recreate -d nginx
echo

echo "### Deleting dummy certificate for $domains ###"
docker-compose run --rm --entrypoint "\
  rm -Rf /etc/letsencrypt/live/$domains && \
  rm -Rf /etc/letsencrypt/archive/$domains && \
  rm -Rf /etc/letsencrypt/renewal/$domains.conf" certbot
echo

echo "### Requesting Let's Encrypt certificate for $domains ###"
domain_args=""
for domain in "${domains[@]}"; do
  domain_args="$domain_args -d $domain"
done

docker-compose run --rm --entrypoint "\
  certbot certonly --webroot -w /var/lib/letsencrypt \
    $domain_args \
    --email $email \
    --rsa-key-size $rsa_key_size \
    --agree-tos \
    --force-renewal" certbot
echo

echo "### Reloading nginx ###"
docker-compose exec nginx nginx -s reload
