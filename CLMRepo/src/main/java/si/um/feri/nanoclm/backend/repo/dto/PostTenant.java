package si.um.feri.nanoclm.backend.repo.dto;

import java.util.Set;

public record PostTenant(
        String title,
        String uniqueName,
        Set<String> allowedUsers) {}
