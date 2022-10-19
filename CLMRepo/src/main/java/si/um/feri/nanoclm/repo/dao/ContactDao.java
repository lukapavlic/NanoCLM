package si.um.feri.nanoclm.repo.dao;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import si.um.feri.nanoclm.repo.vao.Contact;
import java.util.logging.Logger;

public class ContactDao {

    private static final Logger log = Logger.getLogger(ContactDao.class.toString());

    public ContactDao(MongoTemplate dao) {
        this.dao = dao;
    }

    private MongoTemplate dao;

    /**
     * We do not actually delete - we move document to "deleted" collection
     */
    public void deleteContact(String contactUniqueId, String tenantUniqueName) {
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
    }

}
