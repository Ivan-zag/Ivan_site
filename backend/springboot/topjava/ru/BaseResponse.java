package springboot.topjava.ru;

public class BaseResponse {

    private final String status;
    private final Integer code;

    public BaseResponse(String status, Integer code) {
        this.status = status;
        this.code = code;
    }
//sefsefsefsef
    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }
}
