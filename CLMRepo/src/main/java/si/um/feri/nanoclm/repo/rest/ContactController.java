package si.um.feri.nanoclm.repo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.repo.dao.ContactDao;
import si.um.feri.nanoclm.repo.dao.TenantRepository;
import si.um.feri.nanoclm.repo.dto.PostContact;
import si.um.feri.nanoclm.repo.dto.PostProp;
import si.um.feri.nanoclm.repo.events.Event;
import si.um.feri.nanoclm.repo.events.EventType;
import si.um.feri.nanoclm.repo.events.producer.EventNotifyer;
import si.um.feri.nanoclm.repo.rest.security.SecurityManager;
import si.um.feri.nanoclm.repo.vao.Contact;
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
										@RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
										@RequestBody PostContact pc) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		try {
			SecurityManager.checkUserActOnTenant(tenantDao,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}
		//insert
		Contact ret=new Contact(pc);
		ret.generateUniqueId(tenantUniqueName);
		final Contact retFinal=dao.insert(ret,tenantUniqueName);
		log.info(() -> "A new contact created into "+tenantUniqueName+":"+retFinal);
		//log
		eventNotifyer.notify(new Event(
				userId,
				tenantUniqueName,
				ret.getUniqueId(),
				EventType.CONTACT_CREATED,
				LocalDateTime.now(),
				null,
				null,
				ret.toString()));
	    return ResponseEntity.ok(retFinal);
	}

	@PostMapping("/{id}/props")
	public ResponseEntity<Contact> postPropertyToContact(@RequestHeader("userToken") String userToken,
														 @RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
														 @PathVariable("id") String id,
														 @RequestBody PostProp pp) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		try {
			SecurityManager.checkUserActOnTenant(tenantDao,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

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
				userId,
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
														  @RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
														  @PathVariable("id") String id,
														  @RequestBody String attr) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		try {
			SecurityManager.checkUserActOnTenant(tenantDao,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

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
				userId,
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
														@RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
														@PathVariable("id") String id,
														@RequestBody String comment) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		try {
			SecurityManager.checkUserActOnTenant(tenantDao,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

		//verify
		Query q=new Query(Criteria.where("uniqueId").is(id));
		Contact val=dao.findOne(q, Contact.class,tenantUniqueName);
		if (val==null) return ResponseEntity.notFound().build();
		//put comment
		String old=val.toString();
		val.getComments().put(userId.hashCode()+"-"+System.currentTimeMillis(),comment);
		dao.save(val,tenantUniqueName);
		log.info(() -> "A new comment in "+tenantUniqueName+":"+val.getTitle()+"-"+comment);
		//log
		eventNotifyer.notify(new Event(
				userId,
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
										 @RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
										 @PathVariable("id") String id) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		try {
			SecurityManager.checkUserActOnTenant(tenantDao,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

		//verify
		Query q=new Query(Criteria.where("uniqueId").is(id));
		Contact val=dao.findOne(q, Contact.class,tenantUniqueName);
		if (val==null) return ResponseEntity.notFound().build();

		//delete
		new ContactDao(dao, eventNotifyer).deleteContact(id,tenantUniqueName,userId);

	    return ResponseEntity.ok("Deleted");
	}

	@GetMapping()
	public @ResponseBody ResponseEntity<List<Contact>> getAll(@RequestHeader("userToken") String userToken,
											  @RequestHeader("tenantUniqueName") String tenantUniqueNameIn) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		try {
			SecurityManager.checkUserActOnTenant(tenantDao,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

		return ResponseEntity.ok(dao.findAll(Contact.class, tenantUniqueName));
	}


	@GetMapping("/{id}")
	public @ResponseBody ResponseEntity<Contact> getById(@RequestHeader("userToken") String userToken,
														 @RequestHeader("tenantUniqueName") String tenantUniqueNameIn,
														 @PathVariable("id") String id) {
		String tenantUniqueName=tenantUniqueNameIn.toUpperCase();
		String userId=SecurityManager.userIdFromUserToken(userToken);
		//security
		try {
			SecurityManager.checkUserActOnTenant(tenantDao,tenantUniqueName,userId);
		} catch (SecurityManager.TenantNotFoundException e) {
			return ResponseEntity.notFound().build();
		} catch (SecurityManager.UserNotAllowedOnTenantException e) {
			return ResponseEntity.status(405).build(); //not allowed
		}

		Query q=new Query(Criteria.where("uniqueId").is(id));
		Contact val=dao.findOne(q, Contact.class, tenantUniqueName);
		return ResponseEntity.ok(val);
	}

}