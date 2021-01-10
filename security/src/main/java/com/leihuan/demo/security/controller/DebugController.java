package com.leihuan.demo.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DebugController {

    private final RestTemplate restTemplate;

    @Autowired
    public DebugController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/debug")
    public String debugGet() {
        return "Debug Get";
    }

    @PostMapping("/debug")
    public String debugPost(@RequestBody InputBody input) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
            headers.setBasicAuth("admin", "adminAa123456");
            ResponseEntity<String> responseEntity = restTemplate.exchange(input.url, HttpMethod.valueOf(input.method), new HttpEntity<>(input.body, headers), String.class);
            return responseEntity.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    static class InputBody {
        public String url;
        public String method;
        public String body;
    }

}
