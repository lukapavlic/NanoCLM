package si.um.feri.nanoclm.repo.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import si.um.feri.nanoclm.repo.dao.TenantDao;
import si.um.feri.nanoclm.repo.dao.TenantRepository;
import si.um.feri.nanoclm.repo.dto.PostContact;
import si.um.feri.nanoclm.repo.dto.PostProp;
import si.um.feri.nanoclm.repo.dto.PostTenant;
import si.um.feri.nanoclm.repo.events.producer.EventNotifyer;
import si.um.feri.nanoclm.repo.vao.Contact;
import si.um.feri.nanoclm.repo.vao.Tenant;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
class ContactControllerTests {

	@Autowired
	ContactController rest;

	@Autowired
	private TenantRepository tenantDao;

    @Autowired
    private MongoTemplate mongoTemplate;

	@Autowired
	EventNotifyer eventNotifyer;

    private Contact newContact;

	@BeforeEach
	void beforeAll() throws TenantDao.TenantUniqueNameNotAllowedException {
        tenantDao.deleteAll();
        mongoTemplate.getCollection("TENANT").drop();
        mongoTemplate.getCollection("TENANT_DELETED").drop();

        TenantDao dao=new TenantDao(tenantDao,eventNotifyer);
        Tenant inserted=dao.insert(
                new PostTenant("New Tenant","tenant", Set.of("test@user.com")),
                "admin@clm.com"
        );

        newContact=new Contact(new PostContact("John", Map.of("surname","Doe"),Set.of(),Map.of()));
        newContact.generateUniqueId("TENANT");
        newContact=mongoTemplate.insert(newContact,"TENANT");
	}

	@Test
	void createNewTenant() {
		ResponseEntity<Contact> ret=rest.post("TENANT",new PostContact("John", Map.of("surname","Doe"),Set.of(),Map.of()));
		Contact c=ret.getBody();
		Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());
		Assertions.assertEquals("John",c.getTitle());
        Assertions.assertFalse(c.getMongoId().isEmpty());
        Assertions.assertFalse(c.getUniqueId().isEmpty());
        Assertions.assertEquals("Doe",c.getProps().get("surname"));
        Assertions.assertEquals(0,c.getComments().size());
        Assertions.assertEquals(0,c.getAttrs().size());
		Assertions.assertEquals(2,mongoTemplate.count(new Query(),"TENANT"));
	}

    @Test
    void deleteTenant() {
        ResponseEntity<String> ret=rest.delete("TENANT",newContact.getUniqueId());
        String s=ret.getBody();
        Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());
        Assertions.assertEquals("Deleted",s);
        Assertions.assertEquals(0,mongoTemplate.count(new Query(),"TENANT"));
    }

    @Test
    void addProperty() {
        ResponseEntity<Contact> ret=rest.postPropertyToContact("TENANT",newContact.getUniqueId(),
                new PostProp("birthYear","1992"));
        Contact c=ret.getBody();
        Assertions.assertEquals(2,c.getProps().size());
        Assertions.assertEquals("1992",c.getProps().get("birthYear"));
    }

    @Test
    void addAttribute() {
        ResponseEntity<Contact> ret=rest.postAttributeToContact("TENANT",newContact.getUniqueId(),"member2022");
        Contact c=ret.getBody();
        Assertions.assertEquals(1,c.getAttrs().size());
        Assertions.assertTrue(c.getAttrs().contains("member2022"));
    }

    @Test
    void addComment() {
        ResponseEntity<Contact> ret=rest.postCommentToContact("TENANT",newContact.getUniqueId(),"commentor","my comment");
        Contact c=ret.getBody();
        Assertions.assertEquals(1,c.getComments().size());
        Assertions.assertTrue(c.getComments().values().contains("my comment"));
        System.out.println(c);
    }

}