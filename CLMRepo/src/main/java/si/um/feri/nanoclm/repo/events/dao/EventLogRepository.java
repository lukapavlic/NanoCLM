package si.um.feri.nanoclm.repo.events.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.um.feri.nanoclm.repo.events.vao.EventLog;

public interface EventLogRepository extends MongoRepository<EventLog, String> {

}