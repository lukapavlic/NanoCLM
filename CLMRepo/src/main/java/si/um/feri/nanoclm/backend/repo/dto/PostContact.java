package si.um.feri.nanoclm.backend.repo.dto;

import java.util.Map;
import java.util.Set;

public record PostContact(
        String title,
        Map<String, String> props,
        Set<String> attrs,
        Map<String, String> comments) {}
