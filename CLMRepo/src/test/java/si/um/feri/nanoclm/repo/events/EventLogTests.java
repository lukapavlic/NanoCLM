package si.um.feri.nanoclm.repo.events;

import org.awaitility.Awaitility;
import org.awaitility.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import si.um.feri.nanoclm.repo.dao.ContactDao;
import si.um.feri.nanoclm.repo.dao.TenantDao;
import si.um.feri.nanoclm.repo.dao.TenantRepository;
import si.um.feri.nanoclm.repo.dto.PostTenant;
import si.um.feri.nanoclm.repo.events.dao.EventLogRepository;
import si.um.feri.nanoclm.repo.events.producer.EventNotifyer;
import si.um.feri.nanoclm.repo.events.vao.EventLog;
import si.um.feri.nanoclm.repo.vao.Contact;
import si.um.feri.nanoclm.repo.vao.Tenant;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@SpringBootTest
@ActiveProfiles("test")
class EventLogTests {

    private static final Logger log = Logger.getLogger(EventLogTests.class.toString());

    @Autowired
    private TenantRepository tenantRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    EventNotifyer eventNotifyer;

	@Autowired
	private EventLogRepository logRepo;

    private Contact johnContact;

    @BeforeEach
    void beforeAll() throws TenantDao.TenantUniqueNameNotAllowedException {
        mongoTemplate.getCollection("eventlog").drop();
        mongoTemplate.getCollection("tenants").drop();
        mongoTemplate.getCollection("TENANT").drop();
        mongoTemplate.getCollection("TENANT_DELETED").drop();

        TenantDao dao=new TenantDao(tenantRepo,eventNotifyer);
        Tenant inserted=dao.insert(
                new PostTenant("New Tenant","tenant", Set.of("test@user.com")),
                "admin@clm.com"
        );

        johnContact = new Contact("John");
        johnContact.generateUniqueId("TENANT");
        johnContact = mongoTemplate.insert(johnContact, "TENANT");
    }

    @Test
    void insertTenantCheckLog() throws TenantDao.TenantUniqueNameNotAllowedException, InterruptedException {

        Awaitility.await().atMost(Duration.ONE_SECOND).until(()->{
            return logRepo.count()>0;
        });

        Assertions.assertEquals(1,logRepo.count());
        EventLog evt=logRepo.findAll().get(0);

        Assertions.assertEquals(EventType.TENANT_CREATED,evt.getEventType());
        Assertions.assertEquals("TENANT",evt.getRelatedContent());
        Assertions.assertEquals("admin@clm.com",evt.getUser());
    }

    @Test
    void deleteContactTest() throws InterruptedException {
        ContactDao dao = new ContactDao(mongoTemplate, eventNotifyer);
        dao.deleteContact(johnContact.getUniqueId(),"TENANT","my@company.com");

        Awaitility.await().atMost(Duration.ONE_SECOND).until(()->{
            return logRepo.count()==2;
        });

        //first for tenant, second for deleted contact
        Assertions.assertEquals(2,logRepo.count());

        List<EventLog> evts=logRepo.findByTenantUniqueNameAndEventType("TENANT",EventType.CONTACT_DELETED);
		EventLog evt=evts.get(0);

		Assertions.assertEquals(EventType.CONTACT_DELETED,evt.getEventType());
		Assertions.assertEquals("TENANT",evt.getTenantUniqueName());
		Assertions.assertEquals("my@company.com",evt.getUser());
		Assertions.assertEquals(johnContact.getUniqueId(),evt.getContactUniqueId());
    }

}
