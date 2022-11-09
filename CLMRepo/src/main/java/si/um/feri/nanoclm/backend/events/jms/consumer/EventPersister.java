package si.um.feri.nanoclm.backend.events.jms.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import si.um.feri.nanoclm.backend.events.dto.EventConverter;
import si.um.feri.nanoclm.backend.events.dao.EventLogRepository;
import si.um.feri.nanoclm.backend.events.vao.EventLog;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Logger;

@Component
public class EventPersister implements MessageListener {

    private static final Logger log = Logger.getLogger(EventPersister.class.toString());

    @Autowired
    EventLogRepository dao;

    @Override
    @JmsListener(destination = "${active-mq.topic}")
    public void onMessage(Message message) {
        log.info(() -> "Received in EventPersister: "+message);
        try {
            MapMessage mm=(MapMessage)message;
            dao.save(new EventLog(EventConverter.fromMapMessage(mm)));
        } catch (Exception e) {
            log.warning(() -> "onMessage Exception:"+e+"; mgs:"+message);
        }
    }

}



