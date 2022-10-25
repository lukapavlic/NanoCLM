package si.um.feri.nanoclm.repo.dao;

import si.um.feri.nanoclm.repo.dto.PostTenant;
import si.um.feri.nanoclm.repo.events.Event;
import si.um.feri.nanoclm.repo.events.EventType;
import si.um.feri.nanoclm.repo.events.producer.EventNotifyer;
import si.um.feri.nanoclm.repo.vao.Tenant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class TenantDao {

    private static final Logger log = Logger.getLogger(TenantDao.class.toString());
    public class TenantUniqueNameNotAllowedException extends Exception {}

    public static Set TENANT_UNIQUE_NAME_BLACK_LIST=Set.of("EVENTLOG","TENANTS");

    public TenantDao(TenantRepository repo, EventNotifyer eventNotifyer) {
        this.repo = repo;
        this.eventNotifyer = eventNotifyer;
    }

    private TenantRepository repo;

    private EventNotifyer eventNotifyer;

    public Tenant insert(PostTenant pc, String actingUser) throws TenantUniqueNameNotAllowedException {
        //check blacklist
        if (TENANT_UNIQUE_NAME_BLACK_LIST.contains(pc.uniqueName().toUpperCase()))
            throw new TenantUniqueNameNotAllowedException();
        //find
        Optional<Tenant> te=repo.findByTenantUniqueName(pc.uniqueName().toUpperCase());
        if (!te.isEmpty()) throw new TenantUniqueNameNotAllowedException();
        if (pc.uniqueName().toUpperCase().endsWith("DELETED"))  throw new TenantUniqueNameNotAllowedException();
        //store
        Tenant ret=repo.insert(new Tenant(pc));
        log.info("A new tenant is inserted: "+ret);
        //log
        eventNotifyer.notify(new Event(
                actingUser,
                pc.uniqueName(),null,
                EventType.TENANT_CREATED,
                LocalDateTime.now(),
                null,
                null,
                ret.toString()));
        return ret;
    }

}
