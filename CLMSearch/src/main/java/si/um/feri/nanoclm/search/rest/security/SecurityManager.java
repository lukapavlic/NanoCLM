package si.um.feri.nanoclm.search.rest.security;

public interface SecurityManager {

//    class TenantNotFoundException extends Exception {}
//    class UserNotAllowedOnTenantException extends Exception {}
//
//    static Tenant checkUserActOnTenant(TenantRepository tenantDao, String tenantUniqueName, String userId) throws TenantNotFoundException, UserNotAllowedOnTenantException {
//        Optional<Tenant> ten=tenantDao.findByTenantUniqueName(tenantUniqueName);
//        if (ten.isEmpty()) throw new TenantNotFoundException();
//        if (!ten.get().getAllowedUsers().contains(userId)) throw new UserNotAllowedOnTenantException();
//        return ten.get();
//    }

    static String userIdFromUserToken(String userToken) {
        return  userToken;
    }

}
