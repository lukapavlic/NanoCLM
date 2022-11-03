package si.um.feri.nanoclm.repo.rest.security;

import si.um.feri.nanoclm.repo.dao.TenantRepository;
import si.um.feri.nanoclm.repo.vao.Tenant;
import java.util.Optional;

public interface SecurityManager {

    class TenantNotFoundException extends Exception {}
    class UserNotAllowedOnTenantException extends Exception {}

    static Tenant checkUserActOnTenant(TenantRepository tenantDao, String tenantUniqueName, String userId) throws TenantNotFoundException, UserNotAllowedOnTenantException {
        Optional<Tenant> ten=tenantDao.findByTenantUniqueName(tenantUniqueName);
        if (ten.isEmpty()) throw new TenantNotFoundException();
        if (!ten.get().getAllowedUsers().contains(userId)) throw new UserNotAllowedOnTenantException();
        return ten.get();
    }

    static String userIdFromUserToken(String userToken) {
        return  userToken;
    }

}
