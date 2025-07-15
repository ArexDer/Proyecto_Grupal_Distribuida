package com.app.app_books.rest;

import com.app.app_books.cliente.AuthorRestClient;
import com.app.app_books.dtos.AuthorDto;
import com.app.app_books.dtos.BookDto;
import com.app.app_books.db.Book;
import com.app.app_books.repo.BookRepository;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(path = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
@Transactional
@CrossOrigin
public class BookRest {

    @Value("${server.port}")
    private Integer httpPort;

    @Autowired
    private BookRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RestClient.Builder restClientBuilder;

    private AuthorRestClient service;

    //metodo para crear el cliente HTTP
    @PostConstruct
    private void createAuthorClient() {
        var restClient = restClientBuilder.baseUrl("http://app-authors")
                .build();

        var adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        this.service = factory.createClient(AuthorRestClient.class);
    }

    // GET /books/{isbn}
    // http://localhost:9090/books/2222
    @GetMapping(path = "/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable("isbn") String isbn) {
        var obj = repository.findByIsbnBook(isbn);

        if (obj.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        BookDto ret = new BookDto();
        mapper.map(obj.get(), ret);

        try {
            var authors = service.findById(isbn)
                    .stream()
                    .map(AuthorDto::getName)
                    .toList();

            ret.setAuthors(authors);
        } catch (Exception e) {
            ret.setAuthors(Collections.emptyList());
        }

        return ResponseEntity.ok(ret);
    }

    // GET /books
    //http://localhost:9090/books
    @GetMapping
    public List<BookDto> findAll() {

        return repository.findAll()
                .stream()
                .map(book -> {
                    var dto = new BookDto();
                    mapper.map(book, dto);
                    return dto;
                })
                .map(bookDto -> {
                    try {
                        var authors = service.findById(bookDto.getIsbn())
                                .stream()
                                .map(AuthorDto::getName)
                                .toList();

                        bookDto.setAuthors(authors);
                        return bookDto;
                    } catch (Exception e) {
                        bookDto.setAuthors(Collections.emptyList());
                        return bookDto;
                    }
                })
                .toList();
    }

    // POST /books
    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody Book book) {
        repository.save(book);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // PUT /books/{isbn}
    @PutMapping(path = "/{isbn}")
    public ResponseEntity<Void> update(@PathVariable("isbn") String isbn, @RequestBody Book book) {
        if (repository.existsById(isbn)) {
            book.setIsbn(isbn);
            repository.save(book);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }



// http://localhost:9090/books/2222
@DeleteMapping(path = "/{isbn}")
    public ResponseEntity<Void> delete(@PathVariable("isbn") String isbn) {
        if (!repository.existsById(isbn)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            // Eliminar primero line_items (usando isbn)
            repository.deleteLineItemsByIsbn(isbn);

            // Eliminar relaciones books_authors (usando books_isbn)
            repository.deleteBookAuthorsById(isbn);

            // Eliminar registros de inventory (usando isbn)
            repository.deleteInventoryByIsbn(isbn);

            // Finalmente eliminar el libro
            repository.deleteById(isbn);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}

