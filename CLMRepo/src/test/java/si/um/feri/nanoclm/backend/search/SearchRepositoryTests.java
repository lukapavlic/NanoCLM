package si.um.feri.nanoclm.backend.search;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import si.um.feri.nanoclm.backend.search.dao.SearchRepository;
import si.um.feri.nanoclm.backend.search.vao.Search;
import si.um.feri.nanoclm.backend.search.vao.filter.AttributeFilter;
import si.um.feri.nanoclm.backend.search.vao.filter.PropertyFilter;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
class SearchRepositoryTests {

	@Autowired
	private SearchRepository dao;

	private Search search;

	@BeforeEach
	void beforeAll(){
		dao.deleteAll();
		Search s=new Search(
				"owner@mail.com",
				"II",
				"All Professors",
				"",
				true,
				List.of(
						new AttributeFilter(AttributeFilter.FilterType.ATTRIBUTE_PRESENT,"professor"),
						new PropertyFilter(PropertyFilter.FilterType.EQUALS,"workPlace","teacher")
				));
		search=dao.save(s);
	}

	@Test
	void verifyInitialSearch() {
		Assertions.assertEquals("II",search.getOnTenant());
		Assertions.assertEquals(1,dao.count());
		Assertions.assertNotNull(search.getMongoId());
	}

	@Test
	void getPublicSearches() {
		//another private
		Search s=new Search(
				"owner2@mail.com",
				"II",
				"Almost all",
				"e",
				false,
				List.of());
		dao.save(s);

		List<Search> allPublic=dao.findAllByOnTenantAndPublicSearchOnTenantTrue("ii");
		Assertions.assertEquals(0,allPublic.size());

		allPublic=dao.findAllByOnTenantAndPublicSearchOnTenantTrue("II");
		Assertions.assertEquals(1,allPublic.size());
		Assertions.assertEquals(search.getSearchName(),allPublic.get(0).getSearchName());

		List<Search> allfromUser1=dao.findAllByOwnerAndOnTenant("owner@mail.com", "II");
		Assertions.assertEquals(1,allfromUser1.size());
		Assertions.assertEquals(search.getSearchName(),allfromUser1.get(0).getSearchName());

		List<Search> allfromUser2=dao.findAllByOwnerAndOnTenant("owner2@mail.com", "II");
		Assertions.assertEquals(1,allfromUser2.size());
		Assertions.assertEquals("Almost all",allfromUser2.get(0).getSearchName());
	}

	@Test
	void getSearchesForOwner() {
		//another private
		Search s=new Search(
				"owner@mail.com",
				"II",
				"Almost all",
				"e",
				false,
				List.of());
		dao.save(s);

		List<Search> all=dao.findAllByOwner("owner@mail.com");
		Assertions.assertEquals(2,all.size());

		all=dao.findAllByOwner("someUser");
		Assertions.assertEquals(0,all.size());

		all=dao.findAllByOwnerAndSearchName("owner@mail.com","Almost all");
		Assertions.assertEquals(1,all.size());
		Assertions.assertEquals("Almost all",all.get(0).getSearchName());
	}

}
