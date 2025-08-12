package com.example.demo.services;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class WordToHtmlConverter {

    public String convertDocxToHtml(byte[] docxBytes) throws Exception {
        try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(docxBytes))) {
            XHTMLOptions options = XHTMLOptions.create().indent(2);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                XHTMLConverter.getInstance().convert(document, out, options);
                return out.toString("UTF-8");
            }
        }
    }
}
