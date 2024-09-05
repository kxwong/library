package com.example.demo.model;

import java.sql.Timestamp;
import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Record {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne
  @JoinColumn(name = "book_id")
  private Book book;

  @ManyToOne
  @JoinColumn(name = "borrower_id")
  private Borrower borrower;

  private Timestamp borrowedDate;

  private Timestamp returnedDate;

  public Record() {
    super();
  }

  public Record(long id, Book book, Borrower borrower, Timestamp borrowedDate,
      Timestamp returnedDate) {
    super();
    this.id = id;
    this.book = book;
    this.borrower = borrower;
    this.borrowedDate = borrowedDate;
    this.returnedDate = returnedDate;
  }


  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public Borrower getBorrower() {
    return borrower;
  }

  public void setBorrower(Borrower borrower) {
    this.borrower = borrower;
  }

  public Timestamp getBorrowedDate() {
    return borrowedDate;
  }

  public void setBorrowedDate(Timestamp borrowedDate) {
    this.borrowedDate = borrowedDate;
  }

  public Timestamp getReturnedDate() {
    return returnedDate;
  }

  public void setReturnedDate(Timestamp returnedDate) {
    this.returnedDate = returnedDate;
  }

  @Override
  public int hashCode() {
    return Objects.hash(book, borrowedDate, borrower, id, returnedDate);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Record other = (Record) obj;
    return Objects.equals(book, other.book) && Objects.equals(borrowedDate, other.borrowedDate)
        && Objects.equals(borrower, other.borrower) && Objects.equals(id, other.id)
        && Objects.equals(returnedDate, other.returnedDate);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Record [id=").append(id).append(", book=").append(book).append(", borrower=")
        .append(borrower).append(", borrowedDate=").append(borrowedDate).append(", returnedDate=")
        .append(returnedDate).append("]");
    return builder.toString();
  }

}
