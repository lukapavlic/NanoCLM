package si.um.feri.nanoclm.backend.repo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.backend.repo.dao.ContactDao;
import si.um.feri.nanoclm.backend.repo.dao.TenantRepository;
import si.um.feri.nanoclm.backend.repo.dto.PostContact;
import si.um.feri.nanoclm.backend.repo.dto.PostProp;
import si.um.feri.nanoclm.backend.events.dto.Event;
import si.um.feri.nanoclm.backend.events.vao.EventType;
import si.um.feri.nanoclm.backend.events.jms.producer.EventNotifyer;
import si.um.feri.nanoclm.backend.repo.security.RestMehodAuthorizer;
import si.um.feri.nanoclm.backend.repo.vao.Contact;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Standard request headers:
 * - userToken
 * - tenantUniqueName
 */
@CrossOrigin
@RestController
@RequestMapping("/contacts")
public class ContactController {

	private static final Logger log = Logger.getLogger(ContactController.class.toString());

	@Autowired
	private MongoTemplate dao;

	@Autowired
	private TenantRepository tenantDao;

	@Autowired
	EventNotifyer eventNotifyer;

	@PostMapping()
	public ResponseEntity<Contact> post(@RequestHeader("userToken") String userToken,
										@RequestHeader("tenantUniqueName") String tenantUniqueName,
										@RequestBody PostContact pc) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();
		//insert
		Contact ret=new Contact(pc);
		ret.generateUniqueId(tenantUniqueName);
		final Contact retFinal=dao.insert(ret,tenantUniqueName);
		log.info(() -> "A new contact created into "+tenantUniqueName+":"+retFinal);
		//log
		eventNotifyer.notify(new Event(
				auth.getUserId(),
				tenantUniqueName,
				ret.getUniqueId(),
				EventType.CONTACT_CREATED,
				LocalDateTime.now(),
				ret.getTitle(),
				null,
				ret.toString()));
	    return ResponseEntity.ok(retFinal);
	}

	@PostMapping("/{id}/props")
	public ResponseEntity<Contact> postPropertyToContact(@RequestHeader("userToken") String userToken,
														 @RequestHeader("tenantUniqueName") String tenantUniqueName,
														 @PathVariable("id") String id,
														 @RequestBody PostProp pp) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();
		//verify
		Query q=new Query(Criteria.where("uniqueId").is(id));
		Contact val=dao.findOne(q, Contact.class,tenantUniqueName);
		if (val==null) return ResponseEntity.notFound().build();
		//put prop
		String old=val.toString();
		val.getProps().put(pp.name(),pp.value());
		dao.save(val,tenantUniqueName);
		log.info(() -> "A new contact property set into "+tenantUniqueName+":"+val.getTitle()+"-"+pp.name());
		//log
		eventNotifyer.notify(new Event(
				auth.getUserId(),
				tenantUniqueName,
				val.getUniqueId(),
				EventType.PROPERTY_SET,
				LocalDateTime.now(),
				pp.name()+"="+pp.value(),
				old,
				val.toString()));
		return ResponseEntity.ok(val);
	}

	@PostMapping("/{id}/attrs")
	public ResponseEntity<Contact> postAttributeToContact(@RequestHeader("userToken") String userToken,
														  @RequestHeader("tenantUniqueName") String tenantUniqueName,
														  @PathVariable("id") String id,
														  @RequestBody String attr) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();
		//verify
		Query q=new Query(Criteria.where("uniqueId").is(id));
		Contact val=dao.findOne(q, Contact.class,tenantUniqueName);
		if (val==null) return ResponseEntity.notFound().build();
		//put attribute
		String old=val.toString();
		val.getAttrs().add(attr);
		dao.save(val,tenantUniqueName);
		log.info(() -> "A new contact attribute set into "+tenantUniqueName+":"+val.getTitle()+"-"+attr);
		//log
		eventNotifyer.notify(new Event(
				auth.getUserId(),
				tenantUniqueName,
				val.getUniqueId(),
				EventType.ATTRIBUTE_SET,
				LocalDateTime.now(),
				attr,
				old,
				val.toString()));
		return ResponseEntity.ok(val);
	}

	@PostMapping("/{id}/comments")
	public ResponseEntity<Contact> postCommentToContact(@RequestHeader("userToken") String userToken,
														@RequestHeader("tenantUniqueName") String tenantUniqueName,
														@PathVariable("id") String id,
														@RequestBody String comment) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();
		//verify
		Query q=new Query(Criteria.where("uniqueId").is(id));
		Contact val=dao.findOne(q, Contact.class,tenantUniqueName);
		if (val==null) return ResponseEntity.notFound().build();
		//put comment
		String old=val.toString();
		val.getComments().put(auth.getUserId().hashCode()+"-"+System.currentTimeMillis(),comment);
		dao.save(val,tenantUniqueName);
		log.info(() -> "A new comment in "+tenantUniqueName+":"+val.getTitle()+"-"+comment);
		//log
		eventNotifyer.notify(new Event(
				auth.getUserId(),
				tenantUniqueName,
				val.getUniqueId(),
				EventType.COMMENT_SET,
				LocalDateTime.now(),
				comment,
				old,
				val.toString()));
		return ResponseEntity.ok(val);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@RequestHeader("userToken") String userToken,
										 @RequestHeader("tenantUniqueName") String tenantUniqueName,
										 @PathVariable("id") String id) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();
		//verify
		Query q=new Query(Criteria.where("uniqueId").is(id));
		Contact val=dao.findOne(q, Contact.class,tenantUniqueName);
		if (val==null) return ResponseEntity.notFound().build();
		//delete
		new ContactDao(dao, eventNotifyer).deleteContact(id,tenantUniqueName, auth.getUserId());

	    return ResponseEntity.ok("Deleted");
	}

	@GetMapping()
	public @ResponseBody ResponseEntity<List<Contact>> getAll(@RequestHeader("userToken") String userToken,
											  @RequestHeader("tenantUniqueName") String tenantUniqueName) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();

		return ResponseEntity.ok(dao.findAll(Contact.class, tenantUniqueName));
	}


	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Contact> getById(@RequestHeader("userToken") String userToken,
														 @RequestHeader("tenantUniqueName") String tenantUniqueName,
														 @PathVariable("id") String id) {
		//security
		RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
		if (auth.isFailed()) return auth.getResponse();

		Query q=new Query(Criteria.where("uniqueId").is(id));
		Contact val=dao.findOne(q, Contact.class, tenantUniqueName);
		return ResponseEntity.ok(val);
	}

}