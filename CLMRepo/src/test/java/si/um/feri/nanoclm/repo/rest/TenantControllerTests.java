package si.um.feri.nanoclm.repo.rest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import si.um.feri.nanoclm.repo.dao.TenantDao;
import si.um.feri.nanoclm.repo.dao.TenantRepository;
import si.um.feri.nanoclm.repo.dto.PostTenant;
import si.um.feri.nanoclm.repo.events.producer.EventNotifyer;
import si.um.feri.nanoclm.repo.vao.Tenant;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
class TenantControllerTests {

	@Autowired
	TenantController rest;

	@Autowired
	private TenantRepository tenantDao;

    @Autowired
    private MongoTemplate mongoTemplate;

	@Autowired
	EventNotifyer eventNotifyer;

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
	}

	@Test
	void createNewTenant() {
		ResponseEntity<Tenant> ret=rest.post("user@nanoclm.com",new PostTenant("Title","TTL", Set.of()));
		Tenant t=ret.getBody();
		Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());
		Assertions.assertEquals("TTL",t.getTenantUniqueName());
        Assertions.assertFalse(t.getMongoId().isEmpty());
		Assertions.assertEquals(2,tenantDao.count());
	}

    @Test
    void allowOpsOnTenant() {
        ResponseEntity<Tenant> ret2=rest.postAllowUserManagingTenant("user@nanoclm.com","TENANT","user@email.com");
        Assertions.assertEquals(405,ret2.getStatusCode().value());

        ResponseEntity<Tenant> ret=rest.postAllowUserManagingTenant("test@user.com","TENANT","user@email.com");
        Tenant t=ret.getBody();
        Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());
        Assertions.assertTrue(t.getAllowedUsers().contains("user@email.com"));
        Assertions.assertFalse(t.getAllowedUsers().contains("anotheruser@email.com"));
    }

    @Test
    void revokeOpsOnTenant() {
        ResponseEntity<Tenant> ret=rest.post("test@user.com",new PostTenant("Title","TTL", Set.of("allowmeuser")));
        Tenant t=ret.getBody();
        Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());
        Assertions.assertTrue(t.getAllowedUsers().contains("allowmeuser"));

        ret=rest.postRevokeUserManagingTenant("user@nanoclm.com","TTL","allowmeuser");
        Assertions.assertEquals(405,ret.getStatusCode().value());

        ret=rest.postRevokeUserManagingTenant("allowmeuser","TTL","allowmeuser");
        t=ret.getBody();
        Assertions.assertFalse(t.getAllowedUsers().contains("allowmeuser"));
    }

}