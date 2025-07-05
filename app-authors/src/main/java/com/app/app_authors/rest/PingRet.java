package com.app.app_authors.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ping")
public class PingRet {

    @GetMapping
    public String ping() {
        return "pong AUTHORS";
    }
}