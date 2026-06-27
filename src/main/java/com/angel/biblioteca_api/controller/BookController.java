package com.angel.biblioteca_api.controller;

import com.angel.biblioteca_api.dto.BookRequest;
import com.angel.biblioteca_api.dto.BookResponse;
import com.angel.biblioteca_api.model.Book;
import com.angel.biblioteca_api.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public List<BookResponse> getAll(){
        return bookService.findAll().stream()
                .map(BookResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public BookResponse getByID(@PathVariable Long id){
        return BookResponse.fromEntity(bookService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookRequest request){
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setStock(request.getStock());
        book.setCategory(request.getCategory());
        return BookResponse.fromEntity(bookService.save(book));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        bookService.deleteById(id);
    }
}
