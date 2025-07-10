package com.app.app_books.repo;

import com.app.app_books.db.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Query("SELECT b FROM Book b WHERE b.isbn = ?1")
    Optional<Book> findByIsbnBook(String isbn);

    @Modifying //eSTO LE AÑADI PARA QUE SEA UN METODO DE MODIFICACION INvetario y books
    @Transactional
    @Query(value = "DELETE FROM inventory WHERE isbn = :isbn", nativeQuery = true)
    void deleteInventoryByIsbn(@Param("isbn") String isbn);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM books_authors WHERE books_isbn = :isbn", nativeQuery = true)
    void deleteBookAuthorsById(@Param("isbn") String isbn);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM line_items WHERE isbn = :isbn", nativeQuery = true)
    void deleteLineItemsByIsbn(@Param("isbn") String isbn);
}