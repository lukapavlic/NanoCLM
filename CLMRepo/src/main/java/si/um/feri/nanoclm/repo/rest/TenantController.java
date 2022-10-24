package si.um.feri.nanoclm.repo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.repo.dao.TenantDao;
import si.um.feri.nanoclm.repo.dao.TenantRepository;
import si.um.feri.nanoclm.repo.dto.PostTenant;
import si.um.feri.nanoclm.repo.events.producer.JmsProducer;
import si.um.feri.nanoclm.repo.vao.Tenant;
import java.util.Optional;
import java.util.logging.Logger;

@CrossOrigin
@RestController
@RequestMapping("/tenants")
public class TenantController {

	private static final Logger log = Logger.getLogger(TenantController.class.toString());

	@Autowired
	private TenantRepository repo;

	@Autowired
	JmsProducer jmsProducer;
	
	@PostMapping
	public ResponseEntity<Tenant> post(@RequestBody PostTenant pc) {
		TenantDao dao=new TenantDao(repo,jmsProducer);
		Tenant ret=null;
		try {
			ret=dao.insert(pc,null);
		} catch (TenantDao.TenantUniqueNameNotAllowedException e) {
			return new ResponseEntity("entity-unique-name-not-allowed", HttpStatus.NOT_ACCEPTABLE);
		}
	    return ResponseEntity.ok(ret);
	}

	@GetMapping
	public @ResponseBody Iterable<Tenant> getAll() {
		return repo.findAll();
	}

	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Tenant> getById(@PathVariable("id") String id) {
		Optional<Tenant> ret=repo.findByTenantUniqueName(id.toUpperCase());
		if (ret.isEmpty()) return new ResponseEntity("entity-not-found", HttpStatus.NOT_ACCEPTABLE);
		return ResponseEntity.ok(ret.get());
	}

}