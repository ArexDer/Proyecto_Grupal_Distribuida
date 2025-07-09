package com.app.app_books.cliente;

import com.app.app_books.dtos.AuthorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "/authors")
public interface AuthorRestClient {
    @GetExchange("/{id}")
    AuthorDto findById(@PathVariable("id") Integer id);
}
