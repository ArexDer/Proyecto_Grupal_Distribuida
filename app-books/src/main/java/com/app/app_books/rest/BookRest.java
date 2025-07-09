package com.app.app_books.rest;

import com.app.app_books.cliente.AuthorRestClient;
import com.app.app_books.dtos.AuthorDto;
import com.app.app_books.dtos.BookDto;
import com.app.app_books.db.Book;
import com.app.app_books.repo.BookRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(path = "/books")
@Transactional
public class BookRest {

    private static final Logger log = LoggerFactory.getLogger(BookRest.class);

    @Value("${server.port}")
    private Integer httpPort;

    @Autowired
    private BookRepository repository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RestClient.Builder clientBuilder;

    //metodo para crear el cliente HTTP
    private AuthorRestClient createAuthorClient() {
        RestClient restClient = clientBuilder
                .baseUrl("http://localhost:8080/api/authors") // URL del servicio de autores
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(AuthorRestClient.class);
    }

    // GET /books/{isbn}
    @GetMapping(path = "/{isbn}")
    public ResponseEntity<BookDto> findByIsbn(@PathVariable("isbn") String isbn) {
        var obj = repository.findById(isbn);

        if (obj.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        BookDto ret = new BookDto();
        mapper.map(obj.get(), ret);

        try {
            AuthorRestClient client = createAuthorClient();
            var authors = client.findById(isbn)
                    .stream()
                    .map(AuthorDto::getName)
                    .toList();

            ret.setAuthors(authors);
        } catch (Exception e) {
            log.warn("Error fetching authors for ISBN: {}", isbn, e);
            ret.setAuthors(Collections.emptyList());
        }

        return ResponseEntity.ok(ret);
    }

    // GET /books
    //http://localhost:9090/books
    @GetMapping
    public List<BookDto> findAll() {
        AuthorRestClient client = createAuthorClient();

        return repository.findAll()
                .stream()
                .map(book -> {
                    var dto = new BookDto();
                    mapper.map(book, dto);
                    return dto;
                })
                .map(bookDto -> {
                    try {
                        var authors = client.findById(bookDto.getIsbn())
                                .stream()
                                .map(AuthorDto::getName)
                                .toList();

                        bookDto.setAuthors(authors);
                        return bookDto;
                    } catch (Exception e) {
                        log.warn("Error fetching authors for ISBN: {}", bookDto.getIsbn(), e);
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
}