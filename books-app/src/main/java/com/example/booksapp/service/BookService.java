package com.example.booksapp.service;

import com.example.booksapp.domain.Book;
import com.example.booksapp.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void save(List<Book> books) {
        bookRepository.saveAll(books);
    }

}
