package com.angel.biblioteca_api.repository;

import com.angel.biblioteca_api.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);
}
