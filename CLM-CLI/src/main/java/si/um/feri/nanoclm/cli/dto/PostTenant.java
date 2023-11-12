package si.um.feri.nanoclm.cli.dto;

import java.util.Set;

public record PostTenant(
        String title,
        String uniqueName,
        Set<String> allowedUsers) {}
