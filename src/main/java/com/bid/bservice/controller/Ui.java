package com.bid.bservice.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/ui")
public class Ui {
	@RequestMapping(method = RequestMethod.GET, path = "/live")
    public ResponseEntity<byte[]> index() throws IOException {
    	
        ClassPathResource resource = new ClassPathResource("static/bid.html");
        InputStream inputStream = resource.getInputStream();
        byte[] htmlBytes = inputStream.readAllBytes();
        inputStream.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return ResponseEntity.ok().headers(headers).body(htmlBytes);
    }
}
