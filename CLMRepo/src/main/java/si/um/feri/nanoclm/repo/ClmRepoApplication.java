package si.um.feri.nanoclm.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import si.um.feri.nanoclm.repo.dao.ContactDao;
import si.um.feri.nanoclm.repo.dto.PostTenant;
import si.um.feri.nanoclm.repo.vao.Contact;
import si.um.feri.nanoclm.repo.vao.Tenant;
import java.util.logging.Logger;

@SpringBootApplication
public class ClmRepoApplication implements CommandLineRunner {

	private static final Logger log = Logger.getLogger(ClmRepoApplication.class.toString());

	public static void main(String[] args) {
		SpringApplication.run(ClmRepoApplication.class, args);
	}

	@Autowired
	private MongoTemplate dao;

	@Override
	public void run(String... args) throws Exception {

		if (!dao.collectionExists("tenants")) {
			log.info("Inserting initial demo data...");

			log.info(""+
				dao.insert(new Tenant(new PostTenant("Inštitut za informatiko","ii",null)))
			);

			Contact luka=new Contact("Luka");
			luka.generateUniqueId("ii");

			log.info(""+
				dao.insert(luka,"ii")
			);

			new ContactDao(dao).deleteContact(luka.getUniqueId(),"ii");

			Contact ana=new Contact("Ana");
			ana.getAttrs().add("komisija1");
			ana.getAttrs().add("prijava2022");
			ana.getProps().put("ime","Ana");
			ana.getProps().put("priimek","Priimkovič");
			ana.getComments().put("c1","komentar1");
			ana.getComments().put("c2","komentar2");
			ana.generateUniqueId("ii");
			log.info(""+
				dao.insert(ana,"ii")
			);

			log.info("Done inserting initial demo data...");
		}

	}

}
