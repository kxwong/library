package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.model.Borrower;
import com.example.demo.repository.BorrowerRepository;

@Service
public class BorrowerService {

  @Autowired
  private BorrowerRepository borrowerRepository;

  public Borrower registerBorrower(Borrower borrower) {
    return borrowerRepository.save(borrower);
  }

  public List<Borrower> getAllBorrowers() {
    return borrowerRepository.findAll();
  }

  public Borrower getBorrowerById(Long id) {
    return borrowerRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Borrower not found"));
  }

}

