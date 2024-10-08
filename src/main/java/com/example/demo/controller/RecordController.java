package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Record;
import com.example.demo.service.RecordService;

@RestController
@RequestMapping("/api/borrowers/{borrowerId}/books")
public class RecordController {

  @Autowired
  private RecordService recordService;

  @GetMapping("/borrow/{bookId}")
  public Record borrowBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
    return recordService.borrowBook(borrowerId, bookId);
  }

  @GetMapping("/return/{bookId}")
  public Record returnBook(@PathVariable Long borrowerId, @PathVariable Long bookId) {
    return recordService.returnBook(borrowerId, bookId);
  }

}
