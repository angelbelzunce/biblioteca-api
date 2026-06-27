package com.angel.biblioteca_api.controller;

import com.angel.biblioteca_api.dto.BookRequest;
import com.angel.biblioteca_api.model.Book;
import com.angel.biblioteca_api.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<Book> getAll(){
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Book getByID(@PathVariable Long id){
        return bookService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody BookRequest request){
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setStock(request.getStock());
        book.setCategory(request.getCategory());
        return bookService.save(book);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        bookService.deleteById(id);
    }
}
