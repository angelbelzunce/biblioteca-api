package com.angel.biblioteca_api.service;


import com.angel.biblioteca_api.exception.ResourceNotFoundException;
import com.angel.biblioteca_api.model.Book;
import com.angel.biblioteca_api.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> findAll () {
        return bookRepository.findAll();
    }

    public Book findById(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    public Book save(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())){
            throw new RuntimeException("A book with this ISBN already exists");
        }
        return bookRepository.save(book);
    }

    public void deleteById (Long id){
        bookRepository.deleteById(id);
    }
}
