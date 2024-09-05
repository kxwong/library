package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Book;
import com.example.demo.model.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {

  Optional<Record> findByBookAndReturnedDateIsNull(Book book);

}
