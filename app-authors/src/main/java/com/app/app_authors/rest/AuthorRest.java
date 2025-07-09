package com.app.app_authors.rest;


import com.app.app_authors.db.Author;
import com.app.app_authors.repo.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

//URL http://localhost:8080/api/authors
@RestController
@RequestMapping("/api/authors")
public class AuthorRest {

    @Value("${server.port}")
    private Integer httpPort;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private Environment environment;

    private final AtomicInteger index = new AtomicInteger(1);

    @GetMapping("/{id}")
    public ResponseEntity<Author> findById(@PathVariable("id") Integer id) {
        var obj = authorRepository.findById(id);

        if (obj.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(obj.get());
    }

    // http://localhost:8080/api/authors
    @GetMapping
    public List<Author> findAll() {

//        int valor = index.getAndIncrement();
//        if (valor % 5 != 0) {
//            String msg = String.format("Intento %d , generando error", valor);
//            System.out.println("authors ****** |||| ******* " + msg);
//            throw new RuntimeException(msg);
//        }

        return authorRepository.findAll();
    }

    // http://localhost:8080/api/authors/find/2
    @GetMapping("/find/{isbn}")
    public List<Author> findByBook(@PathVariable("isbn") String isbn) {
        //generar errores
        int valor = index.getAndIncrement();
        if (valor % 5 != 0) {
            String msg = String.format("Intento %d , generando error", valor);
            System.out.println("authors ****** |||| ******* " + msg);
            throw new RuntimeException(msg);
        }

        // Mostrar propiedades del entorno de Spring
        String[] profiles = environment.getActiveProfiles();
        System.out.println("Active profiles: " + String.join(", ", profiles));
        System.out.println("Server port: " + httpPort);

        var ret = authorRepository.findByBook(isbn);
        return ret.stream()
                .map(obj -> {
                    String newName = String.format("%s (%d)", obj.getName(), httpPort);
                    obj.setName(newName);
                    return obj;
                }).toList();
    }
}