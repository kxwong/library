package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Borrower;

public interface BorrowerRepository extends JpaRepository<Borrower, Long> {
}
