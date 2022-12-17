package si.um.feri.nanoclm.backend.search.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.backend.repo.dao.TenantRepository;
import si.um.feri.nanoclm.backend.repo.security.RestMehodAuthorizer;
import si.um.feri.nanoclm.backend.repo.vao.Contact;
import si.um.feri.nanoclm.backend.search.dao.SearchRepository;
import si.um.feri.nanoclm.backend.search.dto.Search;
import si.um.feri.nanoclm.backend.search.dto.SearchResult;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * Standard request headers:
 * - userToken
 * - tenantUniqueName
 */
@CrossOrigin
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchRepository dao;

    @Autowired
    private TenantRepository tenantDao;

    @Autowired
    private MongoTemplate contactDao;

    @PostMapping
    public ResponseEntity<SearchResult> performSearch(@RequestHeader("userToken") String userToken,
                                               @RequestHeader("tenantUniqueName") String tenantUniqueName,
                                               @RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer pageSize,
                                               @RequestBody Search s) {
        //security
        RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
        if (auth.isFailed()) return auth.getResponse();
        //do the search
        return ResponseEntity.ok(doSearching(s,tenantUniqueName,page,pageSize));
    }

    @GetMapping("{searchId}")
    public ResponseEntity<SearchResult> performSearchBySearchId(@RequestHeader("userToken") String userToken,
                                                    @RequestHeader("tenantUniqueName") String tenantUniqueName,
                                                    @RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer pageSize,
                                                    @PathVariable("searchId") String searchMongoId) {
        //security
        RestMehodAuthorizer auth=new RestMehodAuthorizer(tenantDao,userToken,tenantUniqueName);
        if (auth.isFailed()) return auth.getResponse();
        //find
        Optional<si.um.feri.nanoclm.backend.search.vao.Search> s=dao.findById(searchMongoId);
        if (s.isEmpty()) return ResponseEntity.notFound().build();
        if (!s.get().getOwner().equals(auth.getUserId())) return ResponseEntity.status(405).build(); //not allowed
        if (!s.get().getOnTenant().equals(tenantUniqueName)) return ResponseEntity.status(405).build(); //not allowed
        //do the search
        return ResponseEntity.ok(doSearching(s.get().asSearchDto(),tenantUniqueName,page,pageSize));
    }

    private SearchResult doSearching(Search search, String tenant, Integer page, Integer pageSize) {
        int pgSize=100;
        int currentPg=0;
        if (pageSize!=null) pgSize=pageSize.intValue();
        if (page!=null) currentPg=page.intValue();

        int allResults=(int)contactDao.count(new Query(),tenant);

        Sort sort=Sort.by(search.sortBy()).ascending();
        if (search.sortOrientation()==si.um.feri.nanoclm.backend.search.vao.Search.SortOrientation.DESC) 
            sort=Sort.by(search.sortBy()).descending();
        Query query=new Query().with(sort).with(Pageable.ofSize(pgSize).withPage(currentPg));
        List<Contact> ret=contactDao.find(query,Contact.class, tenant);

        return new SearchResult(ret,allResults,currentPg,pgSize);
    }

}
