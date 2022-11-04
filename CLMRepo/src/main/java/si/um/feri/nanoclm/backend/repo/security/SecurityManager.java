package si.um.feri.nanoclm.backend.repo.security;

public interface SecurityManager {

    static String userIdFromUserToken(String userToken) {
        return  userToken;
    }

}
