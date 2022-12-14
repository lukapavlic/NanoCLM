package si.um.feri.nanoclm.backend.rest;

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
import si.um.feri.nanoclm.backend.repo.dao.TenantDao;
import si.um.feri.nanoclm.backend.repo.dao.TenantRepository;
import si.um.feri.nanoclm.backend.repo.dto.PostContact;
import si.um.feri.nanoclm.backend.repo.dto.PostProp;
import si.um.feri.nanoclm.backend.repo.dto.PostTenant;
import si.um.feri.nanoclm.backend.events.jms.producer.EventNotifyer;
import si.um.feri.nanoclm.backend.repo.rest.ContactController;
import si.um.feri.nanoclm.backend.repo.vao.Contact;
import si.um.feri.nanoclm.backend.repo.vao.Tenant;
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
        //wrong user
        ResponseEntity<Contact> ret2=rest.post("user@nanoclm.com","TENANT",new PostContact("John", Map.of("surname","Doe"),Set.of(),Map.of()));
        Assertions.assertEquals(405,ret2.getStatusCode().value());

        //wrong tenant
        ResponseEntity<Contact> ret3=rest.post("test@user.com","TENANTTT",new PostContact("John", Map.of("surname","Doe"),Set.of(),Map.of()));
        Assertions.assertEquals(HttpStatus.NOT_FOUND,ret3.getStatusCode());

        //all ok
        ResponseEntity<Contact> ret=rest.post("test@user.com","TENANT",new PostContact("John", Map.of("surname","Doe"),Set.of(),Map.of()));
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
        //user not allowed
        ResponseEntity<String> ret2=rest.delete("user@nanoclm.com","TENANT",newContact.getUniqueId());
        Assertions.assertEquals(405,ret2.getStatusCode().value());

        //user ok
        ResponseEntity<String> ret=rest.delete("test@user.com","TENANT",newContact.getUniqueId());
        String s=ret.getBody();
        Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());
        Assertions.assertEquals("Deleted",s);
        Assertions.assertEquals(0,mongoTemplate.count(new Query(),"TENANT"));
    }

    @Test
    void addProperty() {
        ResponseEntity<Contact> ret2=rest.postPropertyToContact("user@nanoclm.com","TENANT",newContact.getUniqueId(),
                new PostProp("birthYear","1992"));
        Assertions.assertEquals(405,ret2.getStatusCode().value());

        ResponseEntity<Contact> ret=rest.postPropertyToContact("test@user.com","TENANT",newContact.getUniqueId(),
                new PostProp("birthYear","1992"));
        Contact c=ret.getBody();
        Assertions.assertEquals(2,c.getProps().size());
        Assertions.assertEquals("1992",c.getProps().get("birthYear"));
    }

    @Test
    void addAttribute() {
        ResponseEntity<Contact> ret2=rest.postAttributeToContact("user@nanoclm.com","TENANT",newContact.getUniqueId(),"member2022");
        Assertions.assertEquals(405,ret2.getStatusCode().value());

        ResponseEntity<Contact> ret=rest.postAttributeToContact("test@user.com","TENANT",newContact.getUniqueId(),"member2022");
        Contact c=ret.getBody();
        Assertions.assertEquals(1,c.getAttrs().size());
        Assertions.assertTrue(c.getAttrs().contains("member2022"));
    }

    @Test
    void addComment() {
        ResponseEntity<Contact> ret2=rest.postCommentToContact("user@nanoclm.com","TENANT",newContact.getUniqueId(),"my comment");
        Assertions.assertEquals(405,ret2.getStatusCode().value());

        ResponseEntity<Contact> ret=rest.postCommentToContact("test@user.com","TENANT",newContact.getUniqueId(),"my comment");
        Contact c=ret.getBody();
        Assertions.assertEquals(1,c.getComments().size());
        Assertions.assertTrue(c.getComments().values().contains("my comment"));
    }

}