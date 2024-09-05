package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;


@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @Mock
  private BookRepository bookRepository;

  @InjectMocks
  private BookService bookService;

  private static final Book GIVEN_BOOK = new Book("1234", "Once upon a time", "George");

  @Test
  void testRegisterBook_SameIsbnBooksNotFound() {
    testRegisterBook(new ArrayList<>());
  }

  @Test
  void testRegisterBook_SameIsbnBooksOtherThanSelfNotFound() {
    List<Book> foundBooks = Arrays.asList(GIVEN_BOOK);
    testRegisterBook(foundBooks);
  }

  @Test
  void testRegisterBook_SameIsbnBooksFound_TitleNotMatch() {
    Book foundBook = new Book(1L, "1234", "Once upon a time 2", "George");
    List<Book> foundBooks = Arrays.asList(foundBook);
    testRegisterBook_ExceptionCase(foundBooks);
  }

  @Test
  void testRegisterBook_SameIsbnBooksFound_TitleMatchButAuthorNotMatch() {
    Book foundBook = new Book(1L, "1234", "Once upon a time", "Martin");
    List<Book> foundBooks = Arrays.asList(foundBook);
    testRegisterBook_ExceptionCase(foundBooks);
  }

  @Test
  void testRegisterBook_SameIsbnBooksFound_TitleAndAuthorMatch() {
    Book foundBook = new Book(1L, "1234", "Once upon a time", "George");
    List<Book> foundBooks = Arrays.asList(foundBook);
    testRegisterBook(foundBooks);
  }

  @Test
  void testGetAllBooks() {
    bookService.getAllBooks();

    verify(bookRepository).findAll();
  }

  @Test
  void testGetBookById() {
    Long bookId = 1L;
    when(bookRepository.findById(anyLong())).thenReturn(Optional.of(GIVEN_BOOK));

    Book actual = bookService.getBookById(bookId);

    verify(bookRepository).findById(bookId);
    assertEquals(GIVEN_BOOK, actual);
  }

  @Test
  void testGetBookById_NotFound() {
    Long bookId = 1L;
    when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      bookService.getBookById(bookId);
    });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    verify(bookRepository).findById(bookId);
  }

  void testRegisterBook_ExceptionCase(List<Book> foundBooks) {
    when(bookRepository.findAllByIsbn(anyString())).thenReturn(foundBooks);

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      bookService.registerBook(GIVEN_BOOK);
    });
    assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    verify(bookRepository).findAllByIsbn(GIVEN_BOOK.getIsbn());
    verify(bookRepository, never()).save(GIVEN_BOOK);
  }

  void testRegisterBook(List<Book> foundBooks) {
    when(bookRepository.findAllByIsbn(anyString())).thenReturn(foundBooks);

    bookService.registerBook(GIVEN_BOOK);

    verify(bookRepository).findAllByIsbn(GIVEN_BOOK.getIsbn());
    verify(bookRepository).save(GIVEN_BOOK);
  }

}
