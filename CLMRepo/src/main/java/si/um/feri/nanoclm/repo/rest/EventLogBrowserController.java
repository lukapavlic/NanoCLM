package si.um.feri.nanoclm.repo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.repo.dao.TenantRepository;
import si.um.feri.nanoclm.repo.events.EventType;
import si.um.feri.nanoclm.repo.events.dao.EventLogRepository;
import si.um.feri.nanoclm.repo.events.dto.GetEventSimpleMode;
import si.um.feri.nanoclm.repo.events.vao.EventLog;
import si.um.feri.nanoclm.repo.rest.security.SecurityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Standard request headers:
 * - userToken
 * - tenantUniqueName
 */
@CrossOrigin
@RestController
@RequestMapping("/eventlog")
public class EventLogBrowserController {

	@Autowired
	private EventLogRepository repo;

	@Autowired
	private TenantRepository tenantRepo;

	private List<GetEventSimpleMode> translateEventLogs(List<EventLog> from) {
		List<GetEventSimpleMode> ret=new ArrayList<>();
		from.forEach(e -> ret.add(e.toGetEventSimpleMode()));
		return ret;
	}

	@GetMapping("/{eventId}")
	public @ResponseBody ResponseEntity<EventLog> getEventById(@RequestHeader("userToken") String userToken,
															   @RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
															   @PathVariable("eventId") String eventId) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		try {
			SecurityManager.checkUserActOnTenant(tenantRepo,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

		Optional<EventLog> opt=repo.findById(eventId);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		if (!opt.get().getTenantUniqueName().equals(tenantUniqueName)) return ResponseEntity.status(405).build(); //not allowed

		return ResponseEntity.ok(opt.get());
	}

	@GetMapping("/contact/{contactUniqueId}")
	public @ResponseBody ResponseEntity<List<GetEventSimpleMode>> getEventsByContact(@RequestHeader("userToken") String userToken,
																	 @RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
																	 @PathVariable("contactUniqueId") String contactUniqueId) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		try {
			SecurityManager.checkUserActOnTenant(tenantRepo,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}
		return ResponseEntity.ok(translateEventLogs(
			repo.findByTenantUniqueNameAndContactUniqueId(tenantUniqueName,contactUniqueId)
		));
	}

	@GetMapping("/user")
	public @ResponseBody List<GetEventSimpleMode> getMyEvents(@RequestHeader("tenantUniqueName")String tenantUniqueNameIn,
															  @RequestHeader("userToken") String userToken) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		return translateEventLogs(
				repo.findByTenantUniqueNameAndUser(tenantUniqueName,userId)
		);
	}

	@GetMapping("/eventtype/{eventType}")
	public @ResponseBody ResponseEntity<List<GetEventSimpleMode>> getEventsByEventType(@RequestHeader("userToken") String userToken,
																	   @RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
																	   @PathVariable("eventType") String eventType) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		try {
			SecurityManager.checkUserActOnTenant(tenantRepo,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

		return ResponseEntity.ok(translateEventLogs(
				repo.findByTenantUniqueNameAndEventType(tenantUniqueName,EventType.valueOf(eventType))
		));
	}

}