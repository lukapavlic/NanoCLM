import http from "../http-common";
import ITenantPost from "../types/tenant/tenantPost.type";

class TenantDataService {
    post(data: ITenantPost) {
        return http.post('/tenants', data.tenant, { headers: { userToken: data.userToken } });
    }
}

export default new TenantDataService();