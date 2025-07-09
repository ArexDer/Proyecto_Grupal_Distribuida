package com.app.app_books.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingRest {
    @GetMapping("/ping")
    public String ping() {
        return "Pong gg";
    }
}
