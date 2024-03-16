package ca.ulaval.glo2003.application.assembler;

import ca.ulaval.glo2003.application.dtos.OpenedDto;
import ca.ulaval.glo2003.application.dtos.SearchDto;
import ca.ulaval.glo2003.domain.entity.Opened;
import ca.ulaval.glo2003.domain.search.Search;

public class SearchAssembler {
  public Search fromDto(SearchDto searchDto) {

    return new Search(
        searchDto.getName(),
        searchDto.getOpened() == null
            ? null
            : new Opened(searchDto.getOpened().from(), searchDto.getOpened().to()));
  }

  public SearchDto toDto(Search search) {

    return new SearchDto(
        search.getName(),
        search.getOpened() == null
            ? null
            : new OpenedDto(
                search.getOpened().from() == null ? null : search.getOpened().from(),
                search.getOpened().to() == null ? null : search.getOpened().to()));
  }
}
