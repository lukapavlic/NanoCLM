import http from "../http-common";
import ITenantGet from "../types/tenant/tenantGet.type";
import { ITenantPost, ITenantRevokeAllow } from "../types/tenant/tenantPost.type";
import ITenantResponse from "../types/tenant/tenantResponse.type";

class TenantDataService {
    async post(data: ITenantPost): Promise<ITenantResponse> {
        const response = await http.post('/tenants', data.tenant, { headers: { userToken: data.userToken } });
        return response.data;
    }

    async postRevoke(data: ITenantRevokeAllow): Promise<ITenantResponse> {
        const response = await http.post('/tenants/revoke', data.userName, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }

    async postAllow(data: ITenantRevokeAllow): Promise<ITenantResponse> {
        const response = await http.post('/tenants/allow', data.userName, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }

    async getAll(): Promise<Array<ITenantResponse>> {
        const response = await http.get("/tenants");
        return response.data;
    }

    async getById(data: ITenantGet): Promise<ITenantResponse> {
        const response = await http.get(`/tenants/${data.id}`, { headers: { userToken: data.userToken } });
        return response.data;
    }
}

export default new TenantDataService();