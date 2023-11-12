package si.um.feri.nanoclm.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import si.um.feri.nanoclm.backend.repo.dao.ContactDao;
import si.um.feri.nanoclm.backend.repo.dao.TenantDao;
import si.um.feri.nanoclm.backend.repo.dao.TenantRepository;
import si.um.feri.nanoclm.backend.repo.dto.PostTenant;
import si.um.feri.nanoclm.backend.events.jms.producer.EventNotifyer;
import si.um.feri.nanoclm.backend.repo.vao.Contact;

import java.util.Set;
import java.util.logging.Logger;

@SpringBootApplication
public class ClmRepoApplication implements CommandLineRunner {

	private static final Logger log = Logger.getLogger(ClmRepoApplication.class.toString());

	public static void main(String[] args) {
		SpringApplication.run(ClmRepoApplication.class, args);
	}

	@Autowired
	private MongoTemplate dao;

	@Autowired
	private TenantRepository tenantRepository;

	@Autowired
	EventNotifyer eventNotifyer;

	@Override
	public void run(String... args) throws Exception {

		if (!dao.collectionExists("tenants")) {
			log.info( () -> "Inserting initial demo data...");

			log.info(() -> {
						try {
							return ""+
								new TenantDao(tenantRepository, eventNotifyer).insert(
										new PostTenant(
												"Inštitut za informatiko",
												"II",
												Set.of("testuser")),
										"INITIAL_DEMO_APP");
						} catch (TenantDao.TenantUniqueNameNotAllowedException e) {
							return e.getMessage();
						}
					}
			);

			Contact luka=new Contact("Luka");
			luka.generateUniqueId("II");

			log.info(() -> ""+
				dao.insert(luka,"II")
			);

			new ContactDao(dao, eventNotifyer).deleteContact(luka.getUniqueId(),"II","INITIAL_DEMO_APP");

			Contact tilen=new Contact("Tilen");
			tilen.getAttrs().add("komisija1");
			tilen.getAttrs().add("prijava2022");
			tilen.getProps().put("ime","Tilen");
			tilen.getProps().put("letnikStudija","3");
			tilen.getComments().put("c1","komentar1");
			tilen.getComments().put("c2","komentar2");
			tilen.generateUniqueId("ii");
			log.info(() -> ""+
				dao.insert(tilen,"II")
			);

			log.info(() -> "Done inserting initial demo data...");
		}

	}

}
