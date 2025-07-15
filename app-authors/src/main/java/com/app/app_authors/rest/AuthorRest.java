package com.app.app_authors.rest;

import com.app.app_authors.db.Author;
import com.app.app_authors.repo.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//URL http://localhost:8080/api/authors
@RestController
@RequestMapping("/api/authors")
@CrossOrigin
public class AuthorRest {

    @Value("${server.port}")
    private Integer httpPort;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private Environment environment;

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
        return authorRepository.findAll();
    }

    // http://localhost:8080/api/authors/find/2222   1111 , 3333 , etc.
    @GetMapping("/find/{isbn}")
    public List<Author> findByBook(@PathVariable("isbn") String isbn) {
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

    // POST - Crear nuevo autor
    // http://localhost:8080/api/authors
    @PostMapping
    public ResponseEntity<Author> create(@RequestBody Author author) {
        author.setId(null); // Asegurar que es nuevo
        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.ok(savedAuthor);
    }

    // PUT - Actualizar autor existente
    // http://localhost:8080/api/authors/4
    @PutMapping("/{id}")
    public ResponseEntity<Author> update(@PathVariable Integer id, @RequestBody Author author) {
        if (!authorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        author.setId(id);
        Author updatedAuthor = authorRepository.save(author);
        return ResponseEntity.ok(updatedAuthor);
    }

    // DELETE - Eliminar autor
    // http://localhost:8080/api/authors/6
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!authorRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        authorRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}