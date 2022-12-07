import http from "../http-common";
import ISearch from "../types/search/searchPost.type";
import ISearchResponse from "../types/search/searchResponse.type";
import ISearchGet from "../types/search/searchGet.type";

class SearchDataService {
    async create(data: ISearch): Promise<ISearchResponse> {
        try {
            const response = await http.post("/search", { searchString: data.searchString, filters: data.filters, sortBy: data.sortBy, sortOrientation: data.sortOrientation },
                { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName }, params: { page: data.page, pageSize: data.pageSize } });
            if (response.status !== 200)
                throw new Error('Something went wrong');
            const res: ISearchResponse = await response.data;
            return res;
        } catch (error) {
            throw error;
        }
    }

    async getById(data: ISearchGet): Promise<ISearchResponse> {
        try {
            const response = await http.get(`/search/${data.searchId}`, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName }, params: { page: data.page, pageSize: data.pageSize } });
            if (response.status !== 200)
                throw new Error('Something went wrong');
            const res: ISearchResponse = await response.data;
            return res;
        } catch (error) {
            throw error;
        }
    }
}

export default new SearchDataService();