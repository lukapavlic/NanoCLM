package si.um.feri.nanoclm.repo.events.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.um.feri.nanoclm.repo.events.EventType;
import si.um.feri.nanoclm.repo.events.vao.EventLog;
import java.util.List;

public interface EventLogRepository extends MongoRepository<EventLog, String> {

    List<EventLog> findByTenantUniqueNameAndContactUniqueId(String tenantUniqueName,String contactUniqueId);

    List<EventLog> findByTenantUniqueNameAndUser(String tenantUniqueName,String user);

    List<EventLog> findByTenantUniqueNameAndEventType(String tenantUniqueName, EventType eventType);

}