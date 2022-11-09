package si.um.feri.nanoclm.backend.search.dto;

import si.um.feri.nanoclm.backend.search.vao.Filter;
import java.util.List;

public record Search(String searchString,
                     List<Filter> filters,
                     String sortBy,
                     si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation sortOrientation
                      ) { }
