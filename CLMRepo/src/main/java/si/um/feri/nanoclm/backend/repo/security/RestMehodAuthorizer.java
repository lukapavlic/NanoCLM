package si.um.feri.nanoclm.backend.repo.security;

import org.springframework.http.ResponseEntity;
import si.um.feri.nanoclm.backend.repo.dao.TenantRepository;
import si.um.feri.nanoclm.backend.repo.vao.Tenant;
import java.util.Optional;

public class RestMehodAuthorizer {

    public RestMehodAuthorizer(TenantRepository tenantDao,String userToken, String tenantUniqueName) {
        userId=SecurityManager.userIdFromUserToken(userToken);
        Optional<Tenant> ten=tenantDao.findByTenantUniqueName(tenantUniqueName);
        if (ten.isEmpty()) {
            response=ResponseEntity.notFound().build();
            failed=true;
        } else if (!ten.get().getAllowedUsers().contains(userId)) {
            response=ResponseEntity.status(405).build(); //not allowed
            failed=true;
        }
        if (!ten.isEmpty()) tenant=ten.get();
    }

    private String userId;

    private ResponseEntity response;

    private boolean failed=false;

    private Tenant tenant;

    public boolean isFailed() {
        return failed;
    }

    public String getUserId() {
        return userId;
    }

    public ResponseEntity getResponse() {
        return response;
    }

    public Tenant getTenant() {
        return tenant;
    }

}
