package com.angel.biblioteca_api.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String author;
    private String isbn;
    private Integer stock;
    private String category;
}
