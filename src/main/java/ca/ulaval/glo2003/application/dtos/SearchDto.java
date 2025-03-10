package ca.ulaval.glo2003.application.dtos;

public class SearchDto {
  private String name;
  private OpenedDto opened;

  public SearchDto(String name, OpenedDto opened) {
    this.name = name;
    this.opened = opened;
  }

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
