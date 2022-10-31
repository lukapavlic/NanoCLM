package si.um.feri.nanoclm.repo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.repo.dao.TenantDao;
import si.um.feri.nanoclm.repo.dao.TenantRepository;
import si.um.feri.nanoclm.repo.dto.PostTenant;
import si.um.feri.nanoclm.repo.events.Event;
import si.um.feri.nanoclm.repo.events.EventType;
import si.um.feri.nanoclm.repo.events.producer.EventNotifyer;
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
	private TenantRepository repo;

	@Autowired
	EventNotifyer eventNotifyer;
	
	@PostMapping
	public ResponseEntity<Tenant> post(@RequestBody PostTenant pc) {
		TenantDao dao=new TenantDao(repo, eventNotifyer);
		Tenant ret=null;
		try {
			ret=dao.insert(pc,null);
		} catch (TenantDao.TenantUniqueNameNotAllowedException e) {
			return new ResponseEntity("entity-unique-name-not-allowed", HttpStatus.NOT_ACCEPTABLE);
		}
	    return ResponseEntity.ok(ret);
	}

	@PostMapping("/{id}/allow")
	public ResponseEntity<Tenant> postAllowUserManagingTenant(@PathVariable("id") String tenantUniqueNameIn, @RequestBody String userName) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		//verify
		Optional<Tenant> te=repo.findByTenantUniqueName(tenantUniqueName);
		if (te.isEmpty()) return new ResponseEntity("entity-not-found", HttpStatus.NOT_ACCEPTABLE);
		//store
		String old=te.toString();
		te.get().getAllowedUsers().add(userName);
		repo.save(te.get());
		log.info(() -> "A new tenant allowance inserted: "+userName+" for "+tenantUniqueName);
		//log
		eventNotifyer.notify(new Event(
				null,
				tenantUniqueName,
				null,
				EventType.TENANT_ALLOWANCE_GRANTED,
				LocalDateTime.now(),
				userName,
				old,
				te.get().toString()));
		return ResponseEntity.ok(te.get());
	}

	@PostMapping("/{id}/revoke")
	public ResponseEntity<Tenant> postRevokeUserManagingTenant(@PathVariable("id") String tenantUniqueNameIn, @RequestBody String userName) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		//verify
		Optional<Tenant> te=repo.findByTenantUniqueName(tenantUniqueName);
		if (te.isEmpty()) return new ResponseEntity("entity-not-found", HttpStatus.NOT_ACCEPTABLE);
		//store
		String old=te.toString();
		te.get().getAllowedUsers().remove(userName);
		repo.save(te.get());
		log.info(() -> "A tenant allowance revoked: "+userName+" for "+tenantUniqueName);
		//log
		eventNotifyer.notify(new Event(
				null,
				tenantUniqueName,
				null,
				EventType.TENANT_ALLOWANCE_REVOKED,
				LocalDateTime.now(),
				userName,
				old,
				te.get().toString()));
		return ResponseEntity.ok(te.get());
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