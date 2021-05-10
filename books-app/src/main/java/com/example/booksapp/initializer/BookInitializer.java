package com.example.booksapp.initializer;

import com.example.booksapp.repository.BookRepository;
import com.example.booksapp.domain.Book;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class BookInitializer implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();

        log.info("Starting book initialization ...");

        for (int i = 0; i < 10; i++) {

            Book book = new Book();
            Date myDate = faker.date().past(36500, TimeUnit.DAYS);
            int year = myDate.getYear();
            int currentYear = year + 1900;
            book.setName(faker.book().title());
            book.setIsbn(UUID.randomUUID().toString());
            book.setYear(currentYear);
            book.setTopic(faker.book().genre());

            bookRepository.save(book);

        }
        log.info("... finished book initialization");
    }
}
