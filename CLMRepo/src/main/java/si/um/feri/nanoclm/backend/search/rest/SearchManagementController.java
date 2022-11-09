package si.um.feri.nanoclm.backend.search.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.backend.search.dao.SearchRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import si.um.feri.nanoclm.backend.repo.security.SecurityManager;
import si.um.feri.nanoclm.backend.search.dto.PostSearch;
import si.um.feri.nanoclm.backend.search.vao.Search;

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
    public PostSearch postSearch(@RequestHeader("userToken") String userToken,
                             @RequestHeader("tenantUniqueName") String tenantUniqueName,
                             @RequestBody PostSearch s) {
        String userId=SecurityManager.userIdFromUserToken(userToken);
        Search vao=new Search(s,userId,tenantUniqueName);
        return dao.save(vao).asPostSearchDto();
    }

    @PutMapping("{id}")
    public PostSearch putSearch(@RequestHeader("userToken") String userToken,
                            @RequestHeader("tenantUniqueName") String tenantUniqueName,
                            @PathVariable("id") String searchMongoId,
                            @RequestBody PostSearch s) {
        String userId=SecurityManager.userIdFromUserToken(userToken);
        Search vao=new Search(s,userId,tenantUniqueName);
        vao.setMongoId(searchMongoId);
        return dao.save(vao).asPostSearchDto();
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
    public ResponseEntity<PostSearch> getSearchById(@RequestHeader("userToken") String userToken,
                                                @RequestHeader("tenantUniqueName") String tenantUniqueName,
                                                @PathVariable("id") String searchMongoId) {
        String userId=SecurityManager.userIdFromUserToken(userToken);

        Optional<Search> s=dao.findById(searchMongoId);
        if (s.isEmpty()) return ResponseEntity.notFound().build();
        if (!s.get().getOnTenant().equals(tenantUniqueName)) return ResponseEntity.status(405).build(); //not allowed
        if (!s.get().getOwner().equals(userId) || !s.get().isPublicSearchOnTenant()) return ResponseEntity.status(405).build(); //not allowed

        return ResponseEntity.ok(s.get().asPostSearchDto());
    }

    @GetMapping
    public Set<PostSearch> getAvailableSearches(@RequestHeader("userToken") String userToken,
                                            @RequestHeader("tenantUniqueName") String tenantUniqueName) {
        String userId=SecurityManager.userIdFromUserToken(userToken);

        List<Search> mySearches=dao.findAllByOwner(userId);
        List<Search> publicSearches=dao.findAllByOnTenantAndPublicSearchOnTenantTrue(tenantUniqueName);
        Set<PostSearch> ret=new HashSet<>();

        mySearches.forEach(s->ret.add(s.asPostSearchDto()));
        publicSearches.forEach(s->ret.add(s.asPostSearchDto()));
        return ret;
    }

}
