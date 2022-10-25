package si.um.feri.nanoclm.repo.events.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import si.um.feri.nanoclm.repo.events.Event;
import si.um.feri.nanoclm.repo.events.EventConverter;
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
            log.info("Sending to topic "+ dest+"...");
            //jmsTemplate.send(dest,s -> s.createObjectMessage(msg));
            jmsTemplate.send(dest,s -> {
                MapMessage mm=s.createMapMessage();
                try {
                    EventConverter.populateMapMessage(msg,mm);
                } catch (Exception e) {
                    log.info("sendMessage Error: "+e);
                }
                return mm;
            });
        } catch(Exception e){
            log.info("sendMessage Exception: "+ e.getMessage());
        }
    }

}