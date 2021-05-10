package com.example.booksapp.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BookRequest {

    @NotEmpty
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String isbn;

    @NotEmpty
    private int year;

    @NotEmpty
    private String topic;

}
