package si.um.feri.nanoclm.backend.repo.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import si.um.feri.nanoclm.backend.repo.vao.Tenant;
import java.util.Optional;

public interface TenantRepository extends MongoRepository<Tenant, String> {

    Optional<Tenant> findByTenantUniqueName(String clientUniqueName);

}