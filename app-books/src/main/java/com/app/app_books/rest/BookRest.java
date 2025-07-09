package com.app.app_books.rest;

import com.app.app_books.cliente.AuthorRestClient;
import com.app.app_books.dtos.BookDto;
import com.app.app_books.repo.BookRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/books", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Transactional
public class BookRest {

    @Value("${server.port}")
    private Integer httpPort;

    @Autowired
    private BookRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RestClient.Builder client;

    @GetMapping
    public List<BookDto> findAll() {
        var restClient = client.baseUrl("http://app-authors").build();

        var adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        AuthorRestClient service = factory.createClient(AuthorRestClient.class);

        //version-4-->MP Client Automatica
        return repository.findAll()
                .stream()
                .map(book->{
                    System.out.println("Buscando author con id= " + book.getAuthorId());

                    var author = service.findById(book.getAuthorId());

//                    var author = restClientBuilder.build()
//                            .get()
//                            .uri("http://app-authors/authors/{id}", book.getAuthorId())
//                            .retrieve()
//                            .body(AuthorDto.class);

                    var dto = new BookDto( );

                    mapper.map(book, dto);

                    return dto;
                })
                .toList();
    }

    @GetMapping(path = "/{isbn}")
    public ResponseEntity findById(@PathVariable("isbn") String isbn) {
        var obj = repository.findById(isbn);

        if(obj.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(obj.get());
    }

}
