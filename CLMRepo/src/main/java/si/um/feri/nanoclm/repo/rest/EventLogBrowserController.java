package si.um.feri.nanoclm.repo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.repo.events.EventType;
import si.um.feri.nanoclm.repo.events.dao.EventLogRepository;
import si.um.feri.nanoclm.repo.events.dto.GetEventSimpleMode;
import si.um.feri.nanoclm.repo.events.vao.EventLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/eventlog")
public class EventLogBrowserController {

	@Autowired
	private EventLogRepository repo;

	private List<GetEventSimpleMode> translateEventLogs(List<EventLog> from) {
		List<GetEventSimpleMode> ret=new ArrayList<>();
		from.forEach(e -> ret.add(e.toGetEventSimpleMode()));
		return ret;
	}

	@GetMapping("/{eventId}")
	public @ResponseBody ResponseEntity<EventLog> getEventById(@RequestHeader("tenantUniqueName")String tenantUniqueNameIn, @PathVariable("eventId") String eventId) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		Optional<EventLog> opt=repo.findById(eventId);
		if (opt.isEmpty()) return ResponseEntity.notFound().build();
		if (!opt.get().getTenantUniqueName().equals(tenantUniqueName)) return ResponseEntity.status(405).build(); //not allowed
		return ResponseEntity.ok(opt.get());
	}

	@GetMapping("/contact/{contactUniqueId}")
	public @ResponseBody List<GetEventSimpleMode> getEventsByContact(@RequestHeader("tenantUniqueName")String tenantUniqueNameIn, @PathVariable("contactUniqueId") String contactUniqueId) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		return translateEventLogs(
			repo.findByTenantUniqueNameAndContactUniqueId(tenantUniqueName,contactUniqueId)
		);
	}

	@GetMapping("/user/{userName}")
	public @ResponseBody List<GetEventSimpleMode> getEventsByUser(@RequestHeader("tenantUniqueName")String tenantUniqueNameIn, @PathVariable("userName") String userName) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		return translateEventLogs(
				repo.findByTenantUniqueNameAndUser(tenantUniqueName,userName)
		);
	}

	@GetMapping("/eventtype/{eventType}")
	public @ResponseBody List<GetEventSimpleMode> getEventsByEventType(@RequestHeader("tenantUniqueName")String tenantUniqueNameIn, @PathVariable("eventType") String eventType) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		return translateEventLogs(
				repo.findByTenantUniqueNameAndEventType(tenantUniqueName,EventType.valueOf(eventType))
		);
	}

}