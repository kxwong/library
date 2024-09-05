package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Timestamp;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.model.Book;
import com.example.demo.model.Borrower;
import com.example.demo.model.Record;
import com.example.demo.repository.RecordRepository;

@ExtendWith(MockitoExtension.class)
class RecordServiceTest {

  @Mock
  private RecordRepository recordRepository;

  @Mock
  private BookService bookService;

  @Mock
  private BorrowerService borrowerService;

  @InjectMocks
  private RecordService recordService;

  private static final Borrower GIVEN_BORROWER = new Borrower(1L, "Yamada", "yamada@test.com");
  private static final Book GIVEN_BOOK = new Book(2L, "1234", "Once upon a time", "George");

  @Test
  void testBorrowBook() {
    when(borrowerService.getBorrowerById(anyLong())).thenReturn(GIVEN_BORROWER);
    when(bookService.getBookById(anyLong())).thenReturn(GIVEN_BOOK);
    when(recordRepository.findByBookAndReturnedDateIsNull(any(Book.class)))
        .thenReturn(Optional.empty());
    when(recordRepository.save(any(Record.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Record actual = recordService.borrowBook(GIVEN_BORROWER.getId(), GIVEN_BOOK.getId());

    verify(borrowerService).getBorrowerById(GIVEN_BORROWER.getId());
    verify(bookService).getBookById(GIVEN_BOOK.getId());
    verify(recordRepository).findByBookAndReturnedDateIsNull(GIVEN_BOOK);
    assertEquals(GIVEN_BOOK, actual.getBook());
    assertEquals(GIVEN_BORROWER, actual.getBorrower());
    assertNotNull(actual.getBorrowedDate());
    assertNull(actual.getReturnedDate());
  }

  @Test
  void testBorrowBook_BookIsBorrowed() {
    when(borrowerService.getBorrowerById(anyLong())).thenReturn(GIVEN_BORROWER);
    when(bookService.getBookById(anyLong())).thenReturn(GIVEN_BOOK);
    when(recordRepository.findByBookAndReturnedDateIsNull(any(Book.class)))
        .thenReturn(Optional.of(new Record()));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      recordService.borrowBook(GIVEN_BORROWER.getId(), GIVEN_BOOK.getId());
    });

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    verify(borrowerService).getBorrowerById(GIVEN_BORROWER.getId());
    verify(bookService).getBookById(GIVEN_BOOK.getId());
    verify(recordRepository, never()).save(any());
  }

  @Test
  void returnBook() {
    Record record =
        new Record(3L, GIVEN_BOOK, GIVEN_BORROWER, new Timestamp(System.currentTimeMillis()), null);
    when(borrowerService.getBorrowerById(anyLong())).thenReturn(GIVEN_BORROWER);
    when(bookService.getBookById(anyLong())).thenReturn(GIVEN_BOOK);
    when(recordRepository.findByBookAndReturnedDateIsNull(any(Book.class)))
        .thenReturn(Optional.of(record));
    when(recordRepository.save(any(Record.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    Record actual = recordService.returnBook(GIVEN_BORROWER.getId(), GIVEN_BOOK.getId());

    verify(borrowerService).getBorrowerById(GIVEN_BORROWER.getId());
    verify(bookService).getBookById(GIVEN_BOOK.getId());
    verify(recordRepository).findByBookAndReturnedDateIsNull(GIVEN_BOOK);
    assertNotNull(actual.getReturnedDate());
  }

  @Test
  void returnBook_BookBorrowerAndBookReturnerDifferent() {
    Record record =
        new Record(3L, GIVEN_BOOK, GIVEN_BORROWER, new Timestamp(System.currentTimeMillis()), null);
    Borrower anotherBorrower = new Borrower(3L, "Uchida", "uchida@test.com");
    when(borrowerService.getBorrowerById(anyLong())).thenReturn(anotherBorrower);
    when(bookService.getBookById(anyLong())).thenReturn(GIVEN_BOOK);
    when(recordRepository.findByBookAndReturnedDateIsNull(any(Book.class)))
        .thenReturn(Optional.of(record));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      recordService.returnBook(anotherBorrower.getId(), GIVEN_BOOK.getId());
    });

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    assertEquals("This book was borrowed by another borrower", exception.getReason());
    verify(borrowerService).getBorrowerById(anotherBorrower.getId());
    verify(bookService).getBookById(GIVEN_BOOK.getId());
    verify(recordRepository).findByBookAndReturnedDateIsNull(GIVEN_BOOK);
    verify(recordRepository, never()).save(any());
  }

  @Test
  void returnBook_BookIsNotBorrowed() {
    when(borrowerService.getBorrowerById(anyLong())).thenReturn(GIVEN_BORROWER);
    when(bookService.getBookById(anyLong())).thenReturn(GIVEN_BOOK);
    when(recordRepository.findByBookAndReturnedDateIsNull(any(Book.class)))
        .thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      recordService.returnBook(GIVEN_BORROWER.getId(), GIVEN_BOOK.getId());
    });

    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    assertEquals("No active borrow found for this book", exception.getReason());
    verify(borrowerService).getBorrowerById(GIVEN_BORROWER.getId());
    verify(bookService).getBookById(GIVEN_BOOK.getId());
    verify(recordRepository).findByBookAndReturnedDateIsNull(GIVEN_BOOK);
  }

}
