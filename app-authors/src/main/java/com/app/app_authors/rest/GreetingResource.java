package com.app.app_authors.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class GreetingResource {

    // http://localhost:8080/api/hello
    @GetMapping(produces = "text/plain")
    public String hello() {
        return "Hello World";
    }
}