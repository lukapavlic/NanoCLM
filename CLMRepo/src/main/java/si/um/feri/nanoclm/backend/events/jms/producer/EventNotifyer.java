package si.um.feri.nanoclm.backend.events.jms.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import si.um.feri.nanoclm.backend.events.Event;
import si.um.feri.nanoclm.backend.events.EventConverter;
import javax.jms.MapMessage;
import java.util.logging.Logger;

@Component
public class EventNotifyer {

    private static final Logger log = Logger.getLogger(EventNotifyer.class.toString());

    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${active-mq.topic}")
    private String dest;

    public void notify(Event msg){
        try{
            jmsTemplate.send(dest,s -> {
                log.info(() -> "Sending to topic "+ dest+"..."+msg.relatedContent());
                MapMessage mm=s.createMapMessage();
                try {
                    EventConverter.populateMapMessage(msg,mm);
                } catch (Exception e) {
                    log.warning(() -> "sendMessage Error: "+e);
                }
                return mm;
            });
        } catch(Exception e){
            log.warning(() -> "sendMessage Exception: "+ e.getMessage());
        }
    }

}