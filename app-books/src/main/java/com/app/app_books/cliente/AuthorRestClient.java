package com.app.app_books.cliente;

import com.app.app_books.dtos.AuthorDto;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "/find")
public interface AuthorRestClient {
    @GetExchange("/{isbn}")

    List<AuthorDto> findById(@PathVariable("isbn") String isbn);


}
