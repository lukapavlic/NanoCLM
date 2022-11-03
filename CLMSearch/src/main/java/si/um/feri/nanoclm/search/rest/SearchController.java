package si.um.feri.nanoclm.search.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import si.um.feri.nanoclm.search.dao.SearchRepository;

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

}
