package si.um.feri.nanoclm.repo.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

public record PostContact(
        String title,
        Map<String, String> props,
        Set<String> attrs,
        Map<String, String> comments) {}
