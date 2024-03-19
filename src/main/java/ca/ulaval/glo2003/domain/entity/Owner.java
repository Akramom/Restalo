package ca.ulaval.glo2003.domain.entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Owner {

  @Id private String ownerId;
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

  public List<Restaurant> getRestaurants() {
    return restaurants;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(String ownerId) {
    this.ownerId = ownerId;
  }
}
