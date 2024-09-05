package com.example.demo.model;

import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Borrower {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  @Column(unique = true)
  private String emailAddress;

  private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

  public Borrower() {}

  public Borrower(long id, String name, String emailAddress) {
    super();
    this.id = id;
    this.name = name;
    this.emailAddress = emailAddress;
  }

  public Long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress;
  }

  @Override
  public int hashCode() {
    return Objects.hash(emailAddress, id, name);
  }

  public void validate() {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Name must not be null or empty");
    }

    if (emailAddress == null || emailAddress.trim().isEmpty()) {
      throw new IllegalArgumentException("Email address must not be null or empty");
    }

    if (!emailAddress.matches(EMAIL_REGEX)) {
      throw new IllegalArgumentException("Invalid email address format");
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Borrower other = (Borrower) obj;
    return Objects.equals(emailAddress, other.emailAddress) && id == other.id
        && Objects.equals(name, other.name);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Borrower [id=").append(id).append(", name=").append(name)
        .append(", emailAddress=").append(emailAddress).append("]");
    return builder.toString();
  }

}
