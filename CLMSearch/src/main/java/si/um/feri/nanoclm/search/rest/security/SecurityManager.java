package si.um.feri.nanoclm.search.rest.security;

public interface SecurityManager {

    static String userIdFromUserToken(String userToken) {
        return  userToken;
    }

}