package si.um.feri.nanoclm.repo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.repo.dao.TenantRepository;
import si.um.feri.nanoclm.repo.dto.PostTenant;
import si.um.feri.nanoclm.repo.events.Event;
import si.um.feri.nanoclm.repo.events.EventNotifier;
import si.um.feri.nanoclm.repo.events.EventType;
import si.um.feri.nanoclm.repo.vao.Tenant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@CrossOrigin
@RestController
@RequestMapping("/tenants")
public class TenantController {

	private static final Logger log = Logger.getLogger(TenantController.class.toString());

	@Autowired
	private TenantRepository dao;
	
	@PostMapping
	public ResponseEntity<Tenant> post(@RequestBody PostTenant pc) {
		Tenant ret=dao.insert(new Tenant(pc));
		log.info("A new tenant is inserted: "+ret);
		EventNotifier.getInstance().notify(new Event(
				null,
				pc.uniqueName(),null,
				EventType.TENANT_CREATED,
				LocalDateTime.now(),
				null,
				ret.toString()));
	    return ResponseEntity.ok(ret);
	}

	@GetMapping
	public @ResponseBody Iterable<Tenant> getAll() {
		return dao.findAll();
	}

	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Tenant> getById(@PathVariable("id") String id) {
		Optional<Tenant> ret=dao.findByTenantUniqueName(id);
		if (ret.isEmpty()) return new ResponseEntity("entity-not-found", HttpStatus.NOT_ACCEPTABLE);
		return ResponseEntity.ok(ret.get());
	}

}