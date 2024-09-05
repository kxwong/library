package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;

  public Book registerBook(Book book) {
    bookRepository.findAllByIsbn(book.getIsbn()).stream()
        .filter((foundBook) -> !book.getId().equals(foundBook.getId())).findFirst()
        .ifPresent((foundBook) -> {
          if (!book.getTitle().equals(foundBook.getTitle())
              || !book.getAuthor().equals(foundBook.getAuthor())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(
                "Book with ISBN [%s] must have the same title and author " + "", book.getIsbn()));
          }
        });

    return bookRepository.save(book);
  }

  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public Book getBookById(Long id) {
    return bookRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
  }

}
