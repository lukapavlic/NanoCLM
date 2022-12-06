import http from "../http-common";
import ITenantGet from "../types/tenant/tenantGet.type";
import ITenantPost from "../types/tenant/tenantPost.type";
import ITenantRes from "../types/tenant/tenantRes.type";
import ITenantRevokeAllow from "../types/tenant/tenantRevokeAllow.type";

class TenantDataService {
    async post(data: ITenantPost): Promise<ITenantRes> {
        const response = await http.post('/tenants', data.tenant, { headers: { userToken: data.userToken } });
        return response.data;
    }

    async postRevoke(data: ITenantRevokeAllow): Promise<ITenantRes> {
        const response = await http.post('/tenants/revoke', data.userName, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }

    async postAllow(data: ITenantRevokeAllow): Promise<ITenantRes> {
        const response = await http.post('/tenants/allow', data.userName, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }

    async getAll(): Promise<Array<ITenantRes>> {
        const response = await http.get("/tenants");
        return response.data;
    }

    async getById(data: ITenantGet): Promise<ITenantRes> {
        const response = await http.get(`/tenants/${data.id}`, { headers: { userToken: data.userToken } });
        return response.data;
    }
}

export default new TenantDataService();