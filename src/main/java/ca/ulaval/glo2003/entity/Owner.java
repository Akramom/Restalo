package ca.ulaval.glo2003.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Owner {
  private String noOwner;
  private String lastname;
  private String firstname;
  private String phoneNumer;

  private List<Restaurant> restaurants;

  public Owner(String lastname, String firstname, String phoneNumer) {

    this.noOwner = UUID.randomUUID().toString().substring(0, 8);
    ;
    this.lastname = lastname;
    this.firstname = firstname;
    this.phoneNumer = phoneNumer;
    this.restaurants = new ArrayList<>();
  }

  public Owner(String noOwner, String lastname, String firstname, String phoneNumer) {

    this.noOwner = noOwner;
    this.lastname = lastname;
    this.firstname = firstname;
    this.phoneNumer = phoneNumer;
    this.restaurants = new ArrayList<>();
  }

  public void generateId() {
    this.noOwner = UUID.randomUUID().toString().substring(0, 8);
  }

  public List<Restaurant> getRestaurants() {
    return restaurants;
  }

  public String getNoOwner() {
    return noOwner;
  }

  public void setNoOwner(String noOwner) {
    this.noOwner = noOwner;
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
        + noOwner
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
