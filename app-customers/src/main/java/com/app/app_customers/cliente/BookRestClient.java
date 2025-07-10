package com.app.app_customers.cliente;

import com.app.app_customers.dto.BookDto;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.bind.annotation.PathVariable;

@HttpExchange(url = "http://books-api")
//@HttpExchange(url = "http://localhost:9090")
public interface BookRestClient {

    @GetExchange("/books/{isbn}")
    BookDto findByBook(@PathVariable("isbn") String isbn);
}
