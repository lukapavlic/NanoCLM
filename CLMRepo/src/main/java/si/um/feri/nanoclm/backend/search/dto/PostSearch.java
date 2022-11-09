package si.um.feri.nanoclm.backend.search.dto;

import si.um.feri.nanoclm.backend.search.vao.Filter;
import java.util.List;

public record PostSearch(String mongoId,
                         String searchName,
                         boolean publicSearchOnTenant,
                         String search,
                         List<Filter> filters,
                         String sortBy,
                         si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation sortOrientation,
                         si.um.feri.nanoclm.backend.search.vao.Search.ResultPresentation presentation
                      ) { }
