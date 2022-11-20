import http from "../http-common";
import ISearch from "../types/search/search.type";

class SearchDataService {
    create(data: ISearch) {
        return http.post("/search", data.body, { headers: data.headers, params: data.params });
    }
}

export default new SearchDataService();