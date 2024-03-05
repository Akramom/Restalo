package ca.ulaval.glo2003.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Owner {
  private String ownerId;
  private String lastname;
  private String firstname;
  private String phoneNumer;

  private List<Restaurant> restaurants;

  public Owner(String lastname, String firstname, String phoneNumer) {

    this.ownerId = UUID.randomUUID().toString().substring(0, 8);
    this.lastname = lastname;
    this.firstname = firstname;
    this.phoneNumer = phoneNumer;
    this.restaurants = new ArrayList<>();
  }

  public Owner(String ownerId, String lastname, String firstname, String phoneNumer) {

    this.ownerId = ownerId;
    this.lastname = lastname;
    this.firstname = firstname;
    this.phoneNumer = phoneNumer;
    this.restaurants = new ArrayList<>();
  }

  public Owner(String ownerId) {
    this.ownerId = ownerId;
    this.lastname = "Doe";
    this.firstname = "John";
    this.phoneNumer = "418-222-2222";
    this.restaurants = new ArrayList<>();
  }

  public void generateId() {
    this.ownerId = UUID.randomUUID().toString().substring(0, 8);
  }

  public List<Restaurant> getRestaurants() {
    return restaurants;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getPhoneNumer() {
    return phoneNumer;
  }

  public void setPhoneNumer(String phoneNumer) {
    this.phoneNumer = phoneNumer;
  }

  @Override
  public String toString() {
    return "Owner{"
        + "noOwner="
        + ownerId
        + ", lastname='"
        + lastname
        + '\''
        + ", firsname='"
        + firstname
        + '\''
        + ", phoneNumber='"
        + phoneNumer
        + '\''
        + ", restaurants='"
        + restaurants
        + '\''
        + '}'
        + "\n";
  }
}
