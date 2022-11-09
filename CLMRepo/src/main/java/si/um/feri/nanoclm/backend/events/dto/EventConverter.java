package si.um.feri.nanoclm.backend.events.dto;

import si.um.feri.nanoclm.backend.events.dto.Event;
import si.um.feri.nanoclm.backend.events.vao.EventType;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

public interface EventConverter {

    static void populateMapMessage(Event e, MapMessage mm) throws JMSException {
        mm.setString("user",e.user());
        mm.setString("tenantUniqueName",e.tenantUniqueName());
        mm.setString("contactUniqueId",e.contactUniqueId());
        mm.setString("eventType",e.eventType().toString());
        mm.setLong("timestamp",e.timestamp().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        mm.setString("relatedContent",e.relatedContent());
        mm.setString("oldContent",e.oldContent());
        mm.setString("newContent",e.newContent());
    }

    static Event fromMapMessage(MapMessage mm) throws JMSException {
        return new Event(
            mm.getString("user"),
            mm.getString("tenantUniqueName"),
            mm.getString("contactUniqueId"),
            EventType.valueOf(mm.getString("eventType")),
            LocalDateTime.ofInstant(Instant.ofEpochMilli(mm.getLong("timestamp")),
                    TimeZone.getDefault().toZoneId()),
            mm.getString("relatedContent"),
            mm.getString("oldContent"),
            mm.getString("newContent")
        );
    }

}
