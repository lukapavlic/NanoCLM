import http from "../http-common";
import ISearchMgmtResponse from "../types/searchManagement/searchMgmtResponse.type";

class SearchMgmtDataService {
    async getById(userToken: string, tenantUniqueName: string, id: string): Promise<ISearchMgmtResponse> {
        const response = await http.get(`/searchmgmt/${id}`, { headers: { userToken: userToken, tenantUniqueName: tenantUniqueName } });
        return response.data;
    }

    async updateById(userToken: string, tenantUniqueName: string, id: string, data: ISearchMgmtResponse): Promise<ISearchMgmtResponse> {
        const response = await http.put(`/searchmgmt/${id}`, data, { headers: { userToken: userToken, tenantUniqueName: tenantUniqueName } });
        return response.data;
    }

    async deleteById(userToken: string, tenantUniqueName: string, id: string): Promise<{}> {
        const response = await http.delete(`/searchmgmt/${id}`, { headers: { userToken: userToken, tenantUniqueName: tenantUniqueName } });
        return response.data;
    }

    async getAll(userToken: string, tenantUniqueName: string): Promise<Array<ISearchMgmtResponse>> {
        const response = await http.get(`/searchmgmt`, { headers: { userToken: userToken, tenantUniqueName: tenantUniqueName } });
        return response.data;
    }

    async create(userToken: string, tenantUniqueName: string, data: ISearchMgmtResponse): Promise<ISearchMgmtResponse> {
        const response = await http.post(`/searchmgmt`, data, { headers: { userToken: userToken, tenantUniqueName: tenantUniqueName } });
        return response.data;
    }
}

export default new SearchMgmtDataService();