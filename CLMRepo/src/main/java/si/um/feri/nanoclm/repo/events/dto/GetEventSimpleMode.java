package si.um.feri.nanoclm.repo.events.dto;

import si.um.feri.nanoclm.repo.events.EventType;
import java.time.LocalDateTime;

public record GetEventSimpleMode(
        String eventId,

        String user,
        String contactUniqueId,
        EventType eventType,

        LocalDateTime timestamp,
        String relatedContent
        ) {}

