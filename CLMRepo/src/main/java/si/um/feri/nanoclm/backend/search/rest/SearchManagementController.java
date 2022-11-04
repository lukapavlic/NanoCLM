package si.um.feri.nanoclm.backend.search.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.backend.search.dao.SearchRepository;
import si.um.feri.nanoclm.backend.search.vao.Search;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import si.um.feri.nanoclm.backend.repo.security.SecurityManager;

/**
 * Standard request headers:
 * - userToken
 * - tenantUniqueName
 */
@RestController
@RequestMapping("/searchmgmt")
public class SearchManagementController {

    private static final Logger log = Logger.getLogger(SearchManagementController.class.toString());

    @Autowired
    private SearchRepository dao;

    @PostMapping
    public Search postSearch(@RequestHeader("userToken") String userToken,
                             @RequestHeader("tenantUniqueName") String tenantUniqueName,
                             @RequestBody Search s) {
        String userId=SecurityManager.userIdFromUserToken(userToken);

        s.setOwner(userId);
        s.setOnTenant(tenantUniqueName);

        return dao.save(s);
    }

    @PutMapping("{id}")
    public Search putSearch(@RequestHeader("userToken") String userToken,
                            @RequestHeader("tenantUniqueName") String tenantUniqueName,
                            @PathVariable("id") String searchMongoId,
                            @RequestBody Search s) {
        String userId=SecurityManager.userIdFromUserToken(userToken);

        s.setOwner(userId);
        s.setOnTenant(tenantUniqueName);
        s.setMongoId(searchMongoId);

        return dao.save(s);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteSearch(@RequestHeader("userToken") String userToken,
                                       @RequestHeader("tenantUniqueName") String tenantUniqueName,
                                       @PathVariable("id") String searchMongoId) {
        String userId=SecurityManager.userIdFromUserToken(userToken);

        Optional<Search> s=dao.findById(searchMongoId);
        if (s.isEmpty()) return ResponseEntity.notFound().build();
        if (!s.get().getOwner().equals(userId)) return ResponseEntity.status(405).build(); //not allowed

        dao.deleteById(searchMongoId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Search> getSearchById(@RequestHeader("userToken") String userToken,
                                                @RequestHeader("tenantUniqueName") String tenantUniqueName,
                                                @PathVariable("id") String searchMongoId) {
        String userId=SecurityManager.userIdFromUserToken(userToken);

        Optional<Search> s=dao.findById(searchMongoId);
        if (s.isEmpty()) return ResponseEntity.notFound().build();
        if (!s.get().getOnTenant().equals(tenantUniqueName)) return ResponseEntity.status(405).build(); //not allowed
        if (!s.get().getOwner().equals(userId) || !s.get().isPublicSearchOnTenant()) return ResponseEntity.status(405).build(); //not allowed

        return ResponseEntity.ok(s.get());
    }

    @GetMapping
    public Set<Search> getAvailableSearches(@RequestHeader("userToken") String userToken,
                                            @RequestHeader("tenantUniqueName") String tenantUniqueName) {
        String userId=SecurityManager.userIdFromUserToken(userToken);

        List<Search> mySearches=dao.findAllByOwner(userId);
        List<Search> publicSearches=dao.findAllByOnTenantAndPublicSearchOnTenantTrue(tenantUniqueName);
        Set<Search> ret=new HashSet<>();
        ret.addAll(mySearches);
        ret.addAll(publicSearches);
        return ret;
    }

}
