package ca.ulaval.glo2003.api.assemblers.request;

import ca.ulaval.glo2003.api.request.SearchRequest;
import ca.ulaval.glo2003.application.dtos.SearchDto;

public class SearchRequestAssembler {

  public SearchDto toDto(SearchRequest searchRequest) {
    return new SearchDto(
        searchRequest.getName(),
        searchRequest.getOpened() == null ? null : searchRequest.getOpened());
  }
}
