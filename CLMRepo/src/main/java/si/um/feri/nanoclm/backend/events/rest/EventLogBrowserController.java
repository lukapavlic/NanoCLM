package si.um.feri.nanoclm.backend.events.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.backend.repo.dao.TenantRepository;
import si.um.feri.nanoclm.backend.events.EventType;
import si.um.feri.nanoclm.backend.events.dao.EventLogRepository;
import si.um.feri.nanoclm.backend.events.dto.GetEventSimpleMode;
import si.um.feri.nanoclm.backend.events.vao.EventLog;
import si.um.feri.nanoclm.backend.repo.security.RestMehodAuthorizer;
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
	private TenantRepository tenantDao;

	private List<GetEventSimpleMode> translateEventLogs(List<EventLog> from) {
		List<GetEventSimpleMode> ret=new ArrayList<>();
		from.forEach(e -> ret.add(e.toGetEventSimpleMode()));
		return ret;
	}

	@GetMapping("/{eventId}")
	public @ResponseBody ResponseEntity<EventLog> getEventById(@RequestHeader("userToken") String userToken,
															   @RequestHeader("tenantUniqueName") String tenantUniqueName,
															   @PathVariable("eventId") String eventId) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();

		Optional<EventLog> opt=repo.findById(eventId);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		if (!opt.get().getTenantUniqueName().equals(tenantUniqueName)) return ResponseEntity.status(405).build(); //not allowed

		return ResponseEntity.ok(opt.get());
	}

	@GetMapping("/contact/{contactUniqueId}")
	public @ResponseBody ResponseEntity<List<GetEventSimpleMode>> getEventsByContact(@RequestHeader("userToken") String userToken,
																	 @RequestHeader("tenantUniqueName") String tenantUniqueName,
																	 @PathVariable("contactUniqueId") String contactUniqueId) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();

		return ResponseEntity.ok(translateEventLogs(
			repo.findByTenantUniqueNameAndContactUniqueId(tenantUniqueName,contactUniqueId)
		));
	}

	@GetMapping("/user")
	public @ResponseBody ResponseEntity<List<GetEventSimpleMode>> getMyEvents(@RequestHeader("tenantUniqueName")String tenantUniqueName,
															  @RequestHeader("userToken") String userToken) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();

		return ResponseEntity.ok(translateEventLogs(
				repo.findByTenantUniqueNameAndUser(tenantUniqueName, auth.getUserId())
		));
	}

	@GetMapping("/eventtype/{eventType}")
	public @ResponseBody ResponseEntity<List<GetEventSimpleMode>> getEventsByEventType(@RequestHeader("userToken") String userToken,
																	   @RequestHeader("tenantUniqueName") String tenantUniqueName,
																	   @PathVariable("eventType") String eventType) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();

		return ResponseEntity.ok(translateEventLogs(
				repo.findByTenantUniqueNameAndEventType(tenantUniqueName,EventType.valueOf(eventType))
		));
	}

}