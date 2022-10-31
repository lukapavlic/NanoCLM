package si.um.feri.nanoclm.repo.events.vao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import si.um.feri.nanoclm.repo.events.Event;
import si.um.feri.nanoclm.repo.events.EventType;
import si.um.feri.nanoclm.repo.events.dto.GetEventSimpleMode;
import java.time.LocalDateTime;

@Document("eventlog")
public class EventLog {

    public EventLog() {
    }

    public EventLog(Event e) {
        setEventType(e.eventType());
        setUser(e.user());
        setTimestamp(e.timestamp());
        setContactUniqueId(e.contactUniqueId());
        setTenantUniqueName(e.tenantUniqueName());
        setOldContent(e.oldContent());
        setNewContent(e.newContent());
        setRelatedContent(e.relatedContent());
    }

    public GetEventSimpleMode toGetEventSimpleMode() {
        return new GetEventSimpleMode(
                mongoId,
                user,
                contactUniqueId,
                eventType,
                timestamp,
                relatedContent
        );
    }

    @Id
    private String mongoId;
    private String user;
    private String tenantUniqueName;
    private String contactUniqueId;
    private EventType eventType;
    private LocalDateTime timestamp;
    private String oldContent;
    private String relatedContent;
    private String newContent;

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public String getUser() {
        return user;
    }

    public String getRelatedContent() {
        return relatedContent;
    }

    public void setRelatedContent(String relatedContent) {
        this.relatedContent = relatedContent;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTenantUniqueName() {
        return tenantUniqueName;
    }

    public void setTenantUniqueName(String tenantUniqueName) {
        this.tenantUniqueName = tenantUniqueName;
    }

    public String getContactUniqueId() {
        return contactUniqueId;
    }

    public void setContactUniqueId(String contactUniqueId) {
        this.contactUniqueId = contactUniqueId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getOldContent() {
        return oldContent;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    @Override
    public String toString() {
        return "EventLog{" +
                "mongoId='" + mongoId + '\'' +
                ", user='" + user + '\'' +
                ", tenantUniqueName='" + tenantUniqueName + '\'' +
                ", contactUniqueId='" + contactUniqueId + '\'' +
                ", eventType=" + eventType +
                ", timestamp=" + timestamp +
                ", oldContent='" + oldContent + '\'' +
                ", relatedContent='" + relatedContent + '\'' +
                ", newContent='" + newContent + '\'' +
                '}';
    }

}
