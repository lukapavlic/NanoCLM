package si.um.feri.nanoclm.backend.search;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import si.um.feri.nanoclm.backend.events.jms.producer.EventNotifyer;
import si.um.feri.nanoclm.backend.repo.dao.TenantDao;
import si.um.feri.nanoclm.backend.repo.dao.TenantRepository;
import si.um.feri.nanoclm.backend.repo.dto.PostContact;
import si.um.feri.nanoclm.backend.repo.dto.PostTenant;
import si.um.feri.nanoclm.backend.repo.vao.Contact;
import si.um.feri.nanoclm.backend.repo.vao.Tenant;
import si.um.feri.nanoclm.backend.search.dto.Search;
import si.um.feri.nanoclm.backend.search.dto.SearchResult;
import si.um.feri.nanoclm.backend.search.rest.SearchController;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SpringBootTest
@ActiveProfiles("test")
class SearchControllerTests {

	@Autowired
    SearchController rest;

	@Autowired
	private TenantRepository tenantDao;

    @Autowired
    private MongoTemplate mongoTemplate;

	@Autowired
	EventNotifyer eventNotifyer;

	@BeforeEach
	void beforeAll() throws TenantDao.TenantUniqueNameNotAllowedException {
        tenantDao.deleteAll();
        mongoTemplate.getCollection("TENANT").drop();
        TenantDao dao=new TenantDao(tenantDao,eventNotifyer);
        Tenant inserted=dao.insert(
                new PostTenant("New Tenant","tenant", Set.of("user@clm.com")),"user@clm.com"
        );

        Contact newContact=null;
        newContact=new Contact(new PostContact("John",
                Map.of("surname","Doe", "name", "John", "address", "unknown"),
                Set.of(),Map.of()));
        newContact.generateUniqueId("TENANT");
        mongoTemplate.insert(newContact,"TENANT");

        newContact=new Contact(new PostContact("John 4",
                Map.of("surname","Doe 4", "name", "John 4", "address", "unknown 4"),
                Set.of(),Map.of()));
        newContact.generateUniqueId("TENANT");
        mongoTemplate.insert(newContact,"TENANT");

        newContact=new Contact(new PostContact("John 2",
                Map.of("surname","Doe 2", "name", "John 2", "address", "unknown 2"),
                Set.of(),Map.of()));
        newContact.generateUniqueId("TENANT");
        mongoTemplate.insert(newContact,"TENANT");

        newContact=new Contact(new PostContact("Dummy",Map.of(),Set.of(),Map.of()));
        newContact.generateUniqueId("TENANT");
        mongoTemplate.insert(newContact,"TENANT");


        newContact=new Contact(new PostContact("John 3",
                Map.of("surname","Doe 3", "name", "John 3", "address", "unknown 3"),
                Set.of(),Map.of()));
        newContact.generateUniqueId("TENANT");
        mongoTemplate.insert(newContact,"TENANT");
    }

	@Test
	void searchSortAscending() {
        ResponseEntity<SearchResult> ret=rest.performSearch("user@clm.com","TENANT",0,102,
                new Search("", List.of(),"props.name", si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation.ASC));

        SearchResult res=ret.getBody();
		Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());

        System.out.println(res.results());

        Assertions.assertEquals(5,res.allResults());
        Assertions.assertEquals(0,res.page());
        Assertions.assertEquals(102,res.pageSize());
        Assertions.assertEquals("Dummy",res.results().get(0).getTitle());
        Assertions.assertEquals("John",res.results().get(1).getTitle());
        Assertions.assertEquals("John 2",res.results().get(2).getTitle());
        Assertions.assertEquals("John 3",res.results().get(3).getTitle());
        Assertions.assertEquals("John 4",res.results().get(4).getTitle());
	}

    @Test
    void searchSortDescending() {
        ResponseEntity<SearchResult> ret=rest.performSearch("user@clm.com","TENANT",0,102,
                new Search("", List.of(),"props.name", si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation.DESC));

        SearchResult res=ret.getBody();
        Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());

        System.out.println(res.results());

        Assertions.assertEquals(5,res.allResults());
        Assertions.assertEquals(0,res.page());
        Assertions.assertEquals(102,res.pageSize());
        Assertions.assertEquals("John 4",res.results().get(0).getTitle());
        Assertions.assertEquals("John 3",res.results().get(1).getTitle());
        Assertions.assertEquals("John 2",res.results().get(2).getTitle());
        Assertions.assertEquals("John",res.results().get(3).getTitle());
    }

    @Test
    void searchPaging() {
        ResponseEntity<SearchResult> ret=rest.performSearch("user@clm.com","TENANT",0,4,
                new Search("", List.of(),"props.name", si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation.ASC));

        SearchResult res=ret.getBody();
        Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());

        System.out.println(res.results());

        Assertions.assertEquals(5,res.allResults());
        Assertions.assertEquals(0,res.page());
        Assertions.assertEquals(4,res.pageSize());
        Assertions.assertEquals("Dummy",res.results().get(0).getTitle());
        Assertions.assertEquals("John",res.results().get(1).getTitle());
        Assertions.assertEquals("John 2",res.results().get(2).getTitle());
        Assertions.assertEquals("John 3",res.results().get(3).getTitle());
    }

    @Test
    void searchPagingLastPage() {
        ResponseEntity<SearchResult> ret=rest.performSearch("user@clm.com","TENANT",1,4,
                new Search("", List.of(),"props.name", si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation.ASC));

        SearchResult res=ret.getBody();
        Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());

        System.out.println(res.results());

        Assertions.assertEquals(5,res.allResults());
        Assertions.assertEquals(1,res.page());
        Assertions.assertEquals(4,res.pageSize());
        Assertions.assertEquals("John 4",res.results().get(0).getTitle());
        Assertions.assertEquals(1,res.results().size());
    }

    @Test
    void searchPagingPageAfterLast() {
        ResponseEntity<SearchResult> ret=rest.performSearch("user@clm.com","TENANT",8,4,
                new Search("", List.of(),"props.name", si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation.ASC));

        SearchResult res=ret.getBody();
        Assertions.assertEquals(HttpStatus.OK,ret.getStatusCode());

        System.out.println(res.results());

        Assertions.assertEquals(5,res.allResults());
        Assertions.assertEquals(8,res.page());
        Assertions.assertEquals(4,res.pageSize());
        Assertions.assertEquals(0,res.results().size());
    }

}