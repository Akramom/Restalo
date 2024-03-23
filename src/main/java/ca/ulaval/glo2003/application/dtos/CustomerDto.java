package ca.ulaval.glo2003.application.dtos;

import ca.ulaval.glo2003.util.Constante;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CustomerDto {
  @NotNull(message = Constante.MISSING_CUSTOMER_PARAMETER)
  @NotEmpty(message = Constante.MISSING_CUSTOMER_PARAMETER)
  String name;

  @NotNull(message = Constante.MISSING_CUSTOMER_PARAMETER)
  @NotEmpty(message = Constante.MISSING_CUSTOMER_PARAMETER)
  String email;

  @NotNull(message = Constante.MISSING_CUSTOMER_PARAMETER)
  @NotEmpty(message = Constante.MISSING_CUSTOMER_PARAMETER)
  String phoneNumber;

  public CustomerDto() {}

  public CustomerDto(String name, String email, String phoneNumber) {
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
