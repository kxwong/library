package com.example.demo.model;

import java.util.Objects;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String isbn;

  private String title;

  private String author;

  public Book() {}

  public Book(long id, String isbn, String title, String author) {
    this.id = id;
    this.isbn = isbn;
    this.title = title;
    this.author = author;
  }

  public Book(String isbn, String title, String author) {
    this.isbn = isbn;
    this.title = title;
    this.author = author;
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void validate() {
    if (isbn == null || isbn.trim().isEmpty()) {
      throw new IllegalArgumentException("ISBN must not be null or empty");
    }

    if (title == null || title.trim().isEmpty()) {
      throw new IllegalArgumentException("Title must not be null or empty");
    }

    if (author == null || author.trim().isEmpty()) {
      throw new IllegalArgumentException("Author must not be null or empty");
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(author, id, isbn, title);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Book other = (Book) obj;
    return Objects.equals(author, other.author) && Objects.equals(id, other.id)
        && Objects.equals(isbn, other.isbn) && Objects.equals(title, other.title);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Book [id=").append(id).append(", isbn=").append(isbn).append(", title=")
        .append(title).append(", author=").append(author).append("]");
    return builder.toString();
  }

}
