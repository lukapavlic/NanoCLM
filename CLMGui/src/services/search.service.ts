import http from "../http-common";
import ISearch from "../types/search/searchPost.type";
import ISearchResponse from "../types/search/searchResponse.type";
import ISearchGet from "../types/search/searchGet.type";

class SearchDataService {
    async create(data: ISearch): Promise<ISearchResponse> {
        const response = await http.post("/search", { searchString: data.searchString, filters: data.filters, sortBy: data.sortBy, sortOrientation: data.sortOrientation },
            { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName }, params: { page: data.page, pageSize: data.pageSize } });
        return response.data;
    }

    async getById(data: ISearchGet): Promise<ISearchResponse> {
        const response = await http.get(`/search/${data.searchId}`, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName }, params: { page: data.page, pageSize: data.pageSize } });
        return response.data;
    }
}

export default new SearchDataService();