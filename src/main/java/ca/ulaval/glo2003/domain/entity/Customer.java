package ca.ulaval.glo2003.domain.entity;

import dev.morphia.annotations.Entity;
import java.util.Objects;

@Entity
public class Customer {
  private String name;
  private String email;
  private String phoneNumber;

  public Customer(String name, String email, String phoneNumber) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  public Customer() {}

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Customer customer)) return false;
    return Objects.equals(name, customer.name)
        && Objects.equals(email, customer.email)
        && Objects.equals(phoneNumber, customer.phoneNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, email, phoneNumber);
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }
}
