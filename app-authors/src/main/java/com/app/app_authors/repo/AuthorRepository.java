package com.app.app_authors.repo;


import com.app.app_authors.db.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query("SELECT ba.author FROM BookAuthor ba WHERE ba.id.bookIsbn = :isbn")
    List<Author> findByBook(@Param("isbn") String isbn);
}