package si.um.feri.nanoclm.backend.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import si.um.feri.nanoclm.backend.repo.dto.PostTenant;
import si.um.feri.nanoclm.backend.events.jms.producer.EventNotifyer;
import si.um.feri.nanoclm.backend.repo.dao.ContactDao;
import si.um.feri.nanoclm.backend.repo.dao.TenantDao;
import si.um.feri.nanoclm.backend.repo.dao.TenantRepository;
import si.um.feri.nanoclm.backend.repo.vao.Contact;
import si.um.feri.nanoclm.backend.repo.vao.Tenant;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
class ContactDaoTests {

	@Autowired
	private TenantRepository tenantRepo;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	EventNotifyer eventNotifyer;

	@BeforeEach
	void beforeAll() throws TenantDao.TenantUniqueNameNotAllowedException {
		tenantRepo.deleteAll();
		mongoTemplate.getCollection("TENANT").drop();
		mongoTemplate.getCollection("TENANT_DELETED").drop();

		TenantDao dao=new TenantDao(tenantRepo,eventNotifyer);
		Tenant inserted=dao.insert(
				new PostTenant("New Tenant","tenant", Set.of("test@user.com")),
				"admin@clm.com"
		);
	}

	@Test
	void insertContactTest() {
		Contact c=new Contact("John");
		c.generateUniqueId("TENANT");
		c=mongoTemplate.insert(c,"TENANT");

		Assertions.assertEquals(1,mongoTemplate.count(new Query(),"TENANT"));
		Assertions.assertNotNull(c.getMongoId());
	}

	@Test
	void deleteContactTest() {
		ContactDao dao = new ContactDao(mongoTemplate, eventNotifyer);

		Contact c = new Contact("John");
		c.generateUniqueId("TENANT");
		c = mongoTemplate.insert(c, "TENANT");

		Assertions.assertEquals(1, mongoTemplate.count(new Query(), "TENANT"));
		Assertions.assertEquals(0, mongoTemplate.count(new Query(), "TENANT_DELETED"));

		dao.deleteContact(c.getUniqueId(),"TENANT","my@company.com");

		Assertions.assertEquals(0, mongoTemplate.count(new Query(), "TENANT"));
		Assertions.assertEquals(1, mongoTemplate.count(new Query(), "TENANT_DELETED"));
	}

}
