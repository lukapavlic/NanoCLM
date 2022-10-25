package si.um.feri.nanoclm.repo.dao;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import si.um.feri.nanoclm.repo.events.Event;
import si.um.feri.nanoclm.repo.events.EventType;
import si.um.feri.nanoclm.repo.events.producer.EventNotifyer;
import si.um.feri.nanoclm.repo.vao.Contact;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class ContactDao {

    private static final Logger log = Logger.getLogger(ContactDao.class.toString());

    public ContactDao(MongoTemplate dao, EventNotifyer eventNotifyer) {
        this.eventNotifyer = eventNotifyer;
        this.dao = dao;
    }

    private MongoTemplate dao;

    private EventNotifyer eventNotifyer;

    /**
     * We do not actually delete - we move document to "deleted" collection
     */
    public void deleteContact(String contactUniqueId, String tenantUniqueName, String actingUser) {
        //verify
        Query q=new Query(Criteria.where("uniqueId").is(contactUniqueId));
        Contact val=dao.findOne(q, Contact.class,tenantUniqueName);
        if (val==null) return;
        //delete
        val.setMongoId(null);
        val=dao.insert(val,tenantUniqueName+"_deleted");
        log.info("A contact was moved to "+tenantUniqueName+"_deleted:"+val);
        dao.remove(q,tenantUniqueName);
        log.info("A contact was removed from "+tenantUniqueName+":"+contactUniqueId+" - "+val.getTitle());
        //log
        eventNotifyer.notify(new Event(
                actingUser,
                tenantUniqueName,
                contactUniqueId,
                EventType.CONTACT_DELETED,
                LocalDateTime.now(),
                null,
                val.toString(),
                null));
    }

}
