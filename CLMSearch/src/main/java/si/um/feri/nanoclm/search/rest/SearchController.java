package si.um.feri.nanoclm.search.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import si.um.feri.nanoclm.search.dao.SearchRepository;
import si.um.feri.nanoclm.search.vao.Search;
import java.util.List;
import java.util.logging.Logger;

/**
 * Standard request headers:
 * - userToken
 * - tenantUniqueName
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    private static final Logger log = Logger.getLogger(SearchController.class.toString());

    @Autowired
    private SearchRepository dao;

    @PostMapping
    ResponseEntity<List<?>> performSearch(@RequestHeader("userToken") String userToken,
                                          @RequestHeader("tenantUniqueName") String tenantUniqueName,
                                          @RequestParam(required = false) Integer page,
                                          @RequestParam(required = false) Integer pageSize,
                                          @RequestBody Search s) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("{searchId}")
    ResponseEntity<List<?>> performSearchBySearchId(@RequestHeader("userToken") String userToken,
                                                    @RequestHeader("tenantUniqueName") String tenantUniqueName,
                                                    @RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer pageSize,
                                                    @PathVariable("searchId") String searchMongoId) {

        return ResponseEntity.ok().build();
    }


}
