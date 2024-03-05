package ca.ulaval.glo2003.application.dtos;

import ca.ulaval.glo2003.domain.entity.Opened;

public class SearchDto {
  private String name;
  private Opened opened;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Opened getOpened() {
    return opened;
  }

  public void setOpened(Opened opened) {
    this.opened = opened;
  }
}
