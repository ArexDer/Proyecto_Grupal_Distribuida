package com.app.app_authors.db;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class BookAuthorId {

    @Column(name = "books_isbn", nullable = false)
    private String bookIsbn;

    @Column(name = "authors_id", nullable = false)
    private Integer authorId;

}
