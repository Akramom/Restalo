package ca.ulaval.glo2003.api.request;

import ca.ulaval.glo2003.application.dtos.OpenedDto;

public class SearchRequest {
  private String name;
  private OpenedDto opened;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public OpenedDto getOpened() {
    return opened;
  }

  public void setOpened(OpenedDto opened) {
    this.opened = opened;
  }
}
