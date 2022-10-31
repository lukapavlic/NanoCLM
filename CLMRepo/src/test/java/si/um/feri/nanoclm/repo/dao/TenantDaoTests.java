package si.um.feri.nanoclm.repo.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import si.um.feri.nanoclm.repo.dto.PostTenant;
import si.um.feri.nanoclm.repo.events.producer.EventNotifyer;
import si.um.feri.nanoclm.repo.vao.Tenant;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
class TenantDaoTests {

	@Autowired
	private TenantRepository repo;

	@Autowired
	EventNotifyer eventNotifyer;

	@BeforeEach
	void beforeAll(){
		repo.deleteAll();
	}

	@Test
	void insertTenantTest() throws TenantDao.TenantUniqueNameNotAllowedException {
		TenantDao dao=new TenantDao(repo,eventNotifyer);
		Tenant inserted=dao.insert(
				new PostTenant("New Tenant","tenant", Set.of("test@user.com")),
				"admin@clm.com"
		);
		Assertions.assertEquals("TENANT",inserted.getTenantUniqueName());
		Assertions.assertEquals(1,repo.count());
	}

	@Test
	void duplicateTenantTest() {
		TenantDao.TenantUniqueNameNotAllowedException e = Assertions.assertThrows(
			TenantDao.TenantUniqueNameNotAllowedException.class,
			() -> {
				TenantDao dao=new TenantDao(repo,eventNotifyer);
				Tenant inserted=dao.insert(
						new PostTenant("New Tenant","tenant", Set.of("test@user.com")),
						"admin@clm.com"
				);
				dao.insert(
						new PostTenant("New Tenant","tenant", Set.of("test@user.com")),
						"admin@clm.com"
				);
			}
		);
	}

}
