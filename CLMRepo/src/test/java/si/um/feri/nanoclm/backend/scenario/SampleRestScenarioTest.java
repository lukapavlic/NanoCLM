package si.um.feri.nanoclm.backend.scenario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import si.um.feri.nanoclm.backend.events.dto.GetEventSimpleMode;
import si.um.feri.nanoclm.backend.events.rest.EventLogBrowserController;
import si.um.feri.nanoclm.backend.events.vao.EventType;
import si.um.feri.nanoclm.backend.repo.dao.TenantDao;
import si.um.feri.nanoclm.backend.repo.dao.TenantRepository;
import si.um.feri.nanoclm.backend.repo.dto.PostContact;
import si.um.feri.nanoclm.backend.repo.dto.PostTenant;
import si.um.feri.nanoclm.backend.repo.rest.ContactController;
import si.um.feri.nanoclm.backend.repo.rest.TenantController;
import si.um.feri.nanoclm.backend.repo.vao.Contact;
import si.um.feri.nanoclm.backend.repo.vao.Tenant;
import si.um.feri.nanoclm.backend.search.dto.PostSearch;
import si.um.feri.nanoclm.backend.search.dto.Search;
import si.um.feri.nanoclm.backend.search.dto.SearchResult;
import si.um.feri.nanoclm.backend.search.rest.SearchController;
import si.um.feri.nanoclm.backend.search.rest.SearchManagementController;
import si.um.feri.nanoclm.backend.search.vao.filter.AttributeFilter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SampleRestScenarioTest {


    @Autowired
    TenantController restTenant;

    @Autowired
    ContactController restContact;

    @Autowired
    SearchController restSearch;

    @Autowired
    SearchManagementController restSearchManagement;

    @Autowired
    EventLogBrowserController restEvents;

    @Autowired
    private TenantRepository tenantDao;
    @Autowired
    private MongoTemplate mongoTemplate;

    private Tenant awesomeTenant;

    private Contact awesomeTenantContact1;
    private Contact awesomeTenantContact2;
    private Contact awesomeTenantContact3;
    private Contact awesomeTenantContact4;
    private Contact awesomeTenantContact5;

    @BeforeEach
    void beforeAll() throws TenantDao.TenantUniqueNameNotAllowedException {
        tenantDao.deleteAll();
        mongoTemplate.getCollection("AWESOMECOMPANY").drop();
        mongoTemplate.getCollection("AWESOMECOMPANY_DELETED").drop();

        //luka@nanoclm.com adds a new tenant and allow employee@awesomecompany.com to manage it
        restTenant.post("luka@nanoclm.com",new PostTenant("Awesome Company Inc.","AWESOMECOMPANY",Set.of("employee@awesomecompany.com")));
        //let us allow another employee to manage it - only tenant managers can allow / revoke other users!
        ResponseEntity<Tenant> response=restTenant.postAllowUserManagingTenant("employee@awesomecompany.com","AWESOMECOMPANY","another@awesomecompany.com");
        awesomeTenant=response.getBody();

        //now, let tenant managers add come contacts

        awesomeTenantContact1=restContact.post("employee@awesomecompany.com","AWESOMECOMPANY",
                new PostContact(
                        "John the Hero",
                        Map.of("name","John"),
                        Set.of("hero","fired-in-2021"),
                        Map.of())
        ).getBody();

        awesomeTenantContact2=restContact.post("employee@awesomecompany.com","AWESOMECOMPANY",
                new PostContact(
                        "The Elvis",
                        Map.of("birth-date","1935-01-08",
                                "death-date","1977-08-16",
                                "name","Elvis",
                                "surname","Presley",
                                "middle-name","Aaron"
                        ),
                        Set.of("hero"),
                        Map.of("comment1","Not employee, just a record."))
        ).getBody();

        awesomeTenantContact3=restContact.post("employee@awesomecompany.com","AWESOMECOMPANY",
                new PostContact(
                        "George W. Bush",
                        Map.of("name","George"),
                        Set.of("president"),
                        Map.of("c1","See more: https://www.imdb.com/title/tt1175491/"))
        ).getBody();

        awesomeTenantContact4=restContact.post("another@awesomecompany.com","AWESOMECOMPANY",
                new PostContact(
                        "Daenerys Targaryen",
                        Map.of("gender","F",
                                "name","Daenerys"),
                        Set.of("hero","fromzerotohero"),
                        Map.of())
        ).getBody();

        awesomeTenantContact5=restContact.post("another@awesomecompany.com","AWESOMECOMPANY",
                new PostContact(
                        "Marsellus Wallace",
                        Map.of("name","Marsellus",
                                "surname","Wallace"),
                        Set.of("mafia","deadly-wife-footmassage"),
                        Map.of("mycomment","Vincent Vega to the rescue!"))
        ).getBody();

        //we can alter contacts - adding props, attrs, comments
        //contacts have shared ownership inside tenant!
        awesomeTenantContact5=restContact.postAttributeToContact(
                "employee@awesomecompany.com","AWESOMECOMPANY",
                awesomeTenantContact5.getUniqueId(),
                "doesnt-end-well").getBody();

        awesomeTenantContact3=restContact.postCommentToContact(
                "another@awesomecompany.com","AWESOMECOMPANY",
                awesomeTenantContact3.getUniqueId(),
                "Second Bush in the Office."
        ).getBody();
    }

    @Test
    void letUsSeeIfEverythingIsOK() {
        assertTrue(awesomeTenant.getAllowedUsers().contains("employee@awesomecompany.com"));
        assertTrue(awesomeTenant.getAllowedUsers().contains("another@awesomecompany.com"));
        assertFalse(awesomeTenant.getAllowedUsers().contains("withoutAccessToCLM@awesomecompany.com"));

        //we have 5 contacts
        assertEquals(
                5,
                restContact.getAll("employee@awesomecompany.com","AWESOMECOMPANY").getBody().size());

        //contacts are actually in the DB
        assertEquals(
                awesomeTenantContact3.getTitle(),
                restContact.getById("employee@awesomecompany.com","AWESOMECOMPANY",awesomeTenantContact3.getUniqueId())
                        .getBody().getTitle()
        );

        //props, comments, attrs are there
        Contact c=restContact.getById("employee@awesomecompany.com","AWESOMECOMPANY",
                        awesomeTenantContact5.getUniqueId()).getBody();
        assertEquals(3,c.getAttrs().size());
        assertTrue(c.getAttrs().contains("mafia"));
        assertTrue(c.getAttrs().contains("deadly-wife-footmassage"));
        assertTrue(c.getAttrs().contains("doesnt-end-well"));

        assertEquals("Marsellus",c.getProps().get("name"));
        assertNull(c.getProps().get("unknownProp"));

        assertEquals(1,c.getComments().size());
    }

    @Test
    void letUsDoSomeSearching() {
        //first 3 contacts, sort by name, ascending
        SearchResult res=restSearch.performSearch(
                "employee@awesomecompany.com","AWESOMECOMPANY",
                0,3,
                new Search(
                        "",
                        List.of(),
                        "props.name",
                        si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation.ASC)).getBody();

        assertEquals(3, res.results().size());
        assertEquals(5, res.allResults());

        assertEquals("Daenerys", res.results().get(0).getProps().get("name"));
        assertEquals("Elvis", res.results().get(1).getProps().get("name"));
        assertEquals("George", res.results().get(2).getProps().get("name"));

        //next page
        res=restSearch.performSearch(
                "employee@awesomecompany.com","AWESOMECOMPANY",
                1,3,
                new Search(
                        "",
                        List.of(),
                        "props.name",
                        si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation.ASC)).getBody();

        assertEquals(2, res.results().size());
        assertEquals(5, res.allResults());
        assertEquals("John", res.results().get(0).getProps().get("name"));
        assertEquals("Marsellus", res.results().get(1).getProps().get("name"));
    }

    @Test
    void storeAndReuseSearch() {
        //store - only for me; not public on tenant managers
        PostSearch mySearch=
        restSearchManagement.postSearch("employee@awesomecompany.com","AWESOMECOMPANY",
                new PostSearch(null,
                        "All Heroes",true,
                        "",
                        List.of(new AttributeFilter(AttributeFilter.FilterType.ATTRIBUTE_PRESENT,"hero")),
                        "props.name",si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation.ASC,
                        si.um.feri.nanoclm.backend.search.vao.Search.ResultPresentation.LIST
                ));

        //let us take the first that applies
        SearchResult res=restSearch.performSearchBySearchId(
                "employee@awesomecompany.com","AWESOMECOMPANY",
                0,10,mySearch.mongoId()
        ).getBody();

        assertTrue(res.results().get(0).getAttrs().contains("hero"));
    }

    @Test
    void checkActivitiesOnContact() {
        List<GetEventSimpleMode> events=restEvents.getEventsByContact(
                "employee@awesomecompany.com","AWESOMECOMPANY",
                awesomeTenantContact3.getUniqueId()).getBody();
        assertEquals(2,events.size());

        assertEquals(EventType.CONTACT_CREATED,events.get(0).eventType());
        assertEquals("employee@awesomecompany.com",events.get(0).user());

        assertEquals(EventType.COMMENT_SET,events.get(1).eventType());
        assertEquals("another@awesomecompany.com",events.get(1).user());
    }

}
