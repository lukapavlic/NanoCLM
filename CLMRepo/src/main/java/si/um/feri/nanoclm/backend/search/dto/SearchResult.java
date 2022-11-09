package si.um.feri.nanoclm.backend.search.dto;

import si.um.feri.nanoclm.backend.repo.vao.Contact;
import java.util.List;

public record SearchResult(
        List<Contact> results,
        int allResults,
        int page,
        int pageSize
        ) {
}
