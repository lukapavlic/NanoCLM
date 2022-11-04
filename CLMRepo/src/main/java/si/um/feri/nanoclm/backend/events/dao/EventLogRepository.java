package si.um.feri.nanoclm.backend.events.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.um.feri.nanoclm.backend.events.EventType;
import si.um.feri.nanoclm.backend.events.vao.EventLog;
import java.util.List;

public interface EventLogRepository extends MongoRepository<EventLog, String> {

    List<EventLog> findByTenantUniqueNameAndContactUniqueId(String tenantUniqueName,String contactUniqueId);

    List<EventLog> findByTenantUniqueNameAndUser(String tenantUniqueName,String user);

    List<EventLog> findByTenantUniqueNameAndEventType(String tenantUniqueName, EventType eventType);

}