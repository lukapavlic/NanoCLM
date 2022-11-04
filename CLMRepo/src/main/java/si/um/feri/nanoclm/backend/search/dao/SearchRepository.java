package si.um.feri.nanoclm.backend.search.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.um.feri.nanoclm.backend.search.vao.Search;
import java.util.List;

public interface SearchRepository extends MongoRepository<Search, String> {

    List<Search> findAllByOwner(String userId);
    List<Search> findAllByOwnerAndOnTenant(String userId, String tenantId);
    List<Search> findAllByOwnerAndSearchName(String userId, String searchName);
    List<Search> findAllByOnTenantAndPublicSearchOnTenantTrue(String tenant);

}
