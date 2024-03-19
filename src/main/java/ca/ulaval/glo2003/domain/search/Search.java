package ca.ulaval.glo2003.domain.search;

import ca.ulaval.glo2003.domain.entity.Opened;

public class Search {
  private String name;
  private Opened opened;

  public Search(String name, Opened opened) {
    this.name = name;
    this.opened = opened;
  }

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
