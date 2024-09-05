package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Borrower;
import com.example.demo.service.BorrowerService;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

  @Autowired
  private BorrowerService borrowerService;

  @PostMapping
  public Borrower registerBorrower(@RequestBody Borrower borrower) {
    borrower.validate();
    return borrowerService.registerBorrower(borrower);
  }

  @GetMapping
  public List<Borrower> getAllBorrowers() {
    return borrowerService.getAllBorrowers();
  }

}
