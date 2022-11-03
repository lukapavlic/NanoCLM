package si.um.feri.nanoclm.repo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.repo.dao.TenantDao;
import si.um.feri.nanoclm.repo.dao.TenantRepository;
import si.um.feri.nanoclm.repo.dto.PostTenant;
import si.um.feri.nanoclm.repo.events.Event;
import si.um.feri.nanoclm.repo.events.EventType;
import si.um.feri.nanoclm.repo.events.producer.EventNotifyer;
import si.um.feri.nanoclm.repo.rest.security.SecurityManager;
import si.um.feri.nanoclm.repo.vao.Tenant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Standard request headers:
 * - userToken
 * - tenantUniqueName
 */
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
	public ResponseEntity<Tenant> post(@RequestHeader("userToken") String userToken,
									   @RequestBody PostTenant pc) {
		String userId= SecurityManager.userIdFromUserToken(userToken);
		TenantDao dao=new TenantDao(repo, eventNotifyer);
		Tenant ret=null;
		try {
			ret=dao.insert(pc,userId);
		} catch (TenantDao.TenantUniqueNameNotAllowedException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}
	    return ResponseEntity.ok(ret);
	}

	@PostMapping("/allow")
	public ResponseEntity<Tenant> postAllowUserManagingTenant(@RequestHeader("userToken") String userToken,
															  @RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
															  @RequestBody String userName) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		Tenant te=null;
		try {
			te=SecurityManager.checkUserActOnTenant(repo,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

		//store
		String old=te.toString();
		te.getAllowedUsers().add(userName);
		repo.save(te);
		log.info(() -> "A new tenant allowance inserted: "+userName+" for "+tenantUniqueName);
		//log
		eventNotifyer.notify(new Event(
				userId,
				tenantUniqueName,
				null,
				EventType.TENANT_ALLOWANCE_GRANTED,
				LocalDateTime.now(),
				userName,
				old,
				te.toString()));
		return ResponseEntity.ok(te);
	}

	@PostMapping("/revoke")
	public ResponseEntity<Tenant> postRevokeUserManagingTenant(@RequestHeader("userToken") String userToken,
															   @RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
															   @RequestBody String userName) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		Tenant te=null;
		try {
			te=SecurityManager.checkUserActOnTenant(repo,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

		//store
		String old=te.toString();
		te.getAllowedUsers().remove(userName);
		repo.save(te);
		log.info(() -> "A tenant allowance revoked: "+userName+" for "+tenantUniqueName);
		//log
		eventNotifyer.notify(new Event(
				userId,
				tenantUniqueName,
				null,
				EventType.TENANT_ALLOWANCE_REVOKED,
				LocalDateTime.now(),
				userName,
				old,
				te.toString()));
		return ResponseEntity.ok(te);
	}

	@GetMapping
	public @ResponseBody Iterable<Tenant> getAll() {
		return repo.findAll();
	}

	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Tenant> getById(@RequestHeader("userToken") String userToken,
														@PathVariable("id") String id) {
		String tenantUniqueName=id.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		Tenant te=null;
		try {
			te=SecurityManager.checkUserActOnTenant(repo,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

		return ResponseEntity.ok(te);
	}

}