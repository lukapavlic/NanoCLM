package si.um.feri.nanoclm.repo.events;

import java.io.Serializable;
import java.time.LocalDateTime;

public record Event (
        String user,
        String tenantUniqueName,
        String contactUniqueId,
        EventType eventType,
        LocalDateTime timestamp,
        String relatedContent,
        String oldContent,
        String newContent
        ) { }
