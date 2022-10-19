package si.um.feri.nanoclm.repo.events;

import java.util.logging.Logger;

public class EventNotifier {

    private static final Logger log = Logger.getLogger(EventNotifier.class.toString());

    private static EventNotifier instance=new EventNotifier();

    private EventNotifier() {

    }

    public static EventNotifier getInstance() {
        return instance;
    }

    public void notify(Event e) {
        log.info("Notifying on event: "+e);
    }

}
