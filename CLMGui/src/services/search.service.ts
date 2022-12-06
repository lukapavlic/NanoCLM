import http from "../http-common";
import ISearch from "../types/search/search.type";
import ISearchResponse from "../types/search/searchResponse.type";

class SearchDataService {
    async create(data: ISearch): Promise<ISearchResponse> {
        const response = await http.post("/search", data.body, { headers: data.headers, params: data.params });
        return response.data;
    }
}

export default new SearchDataService();