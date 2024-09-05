package com.example.demo.service;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.model.Book;
import com.example.demo.model.Borrower;
import com.example.demo.model.Record;
import com.example.demo.repository.RecordRepository;

@Service
public class RecordService {

  @Autowired
  private RecordRepository recordRepository;

  @Autowired
  private BookService bookService;

  @Autowired
  private BorrowerService borrowerService;

  public Record borrowBook(Long borrowerId, Long bookId) {
    Borrower borrower = borrowerService.getBorrowerById(borrowerId);

    Book book = bookService.getBookById(bookId);

    recordRepository.findByBookAndReturnedDateIsNull(book).ifPresent(b -> {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
          "Book is already borrowed");
    });

    Record record = new Record();
    record.setBook(book);
    record.setBorrower(borrower);
    record.setBorrowedDate(new Timestamp(System.currentTimeMillis()));

    return recordRepository.save(record);
  }

  public Record returnBook(Long borrowerId, Long bookId) {
    Borrower borrower = borrowerService.getBorrowerById(borrowerId);

    Book book = bookService.getBookById(bookId);

    Record record = recordRepository.findByBookAndReturnedDateIsNull(book)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
            "No active borrow found for this book"));

    if (!record.getBorrower().getId().equals(borrower.getId())) {
      throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
          "This book was borrowed by another borrower");
    }

    record.setReturnedDate(new Timestamp(System.currentTimeMillis()));

    return recordRepository.save(record);
  }

}
