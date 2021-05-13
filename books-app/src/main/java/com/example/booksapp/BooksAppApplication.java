package com.example.booksapp;


import com.example.booksapp.domain.Book;
import com.example.booksapp.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import org.apache.catalina.Context;

import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;

import java.io.IOException;
import java.io.InputStream;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@SpringBootApplication
public class BooksAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksAppApplication.class, args);

	}

	@Bean
	public ServletWebServerFactory servletContainer() {
		// Enable SSL Trafic
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};

		// Add HTTP to HTTPS redirect
		tomcat.addAdditionalTomcatConnectors(httpToHttpsRedirectConnector());

		return tomcat;
	}

	/*
    We need to redirect from HTTP to HTTPS. Without SSL, this application used
    port 8080. With SSL it will use port 8443. So, any request for 8080 needs to be
    redirected to HTTPS on 8443.
     */
	private Connector httpToHttpsRedirectConnector() {
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setSecure(false);
		connector.setRedirectPort(8443);
		return connector;
	}

	@Bean
	CommandLineRunner runner(BookService bookService){
		return args -> {
			// read JSON and load json
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Book>> typeReference = new TypeReference<List<Book>>(){};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/json/books.json");
			if (inputStream != null) {
				try {
					List<Book> books = mapper.readValue(inputStream, typeReference);
					bookService.save(books);
					books.sort((o1, o2) -> o1.getYear() - o2.getYear());
					System.out.println("\nSorted list by year:");
					for (Book book : books) {
						System.out.println("Book: "
								+ "id: (" + book.getId() + ")"
								+ " name: (" + book.getName() + ")"
								+ " topic: (" + book.getTopic() + ")"
								+ " isdn: (" + book.getIsbn() + ")"
								+ " year: (" + book.getYear() + ")");
					}
					System.out.println("Books Saved!");

				} catch (IOException e) {
					System.out.println("Unable to save books: " + e.getMessage());
				}

			}
		};
	}
}
