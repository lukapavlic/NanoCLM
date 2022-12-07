import axios, { AxiosResponse } from 'axios';
import SearchDataService from '../services/search.service';
import ISearch from '../types/search/searchPost.type';

jest.mock('axios');

const mockedAxios = axios as jest.Mocked<typeof axios>;

describe('postSearch()', () => {
    test('should return search object', async () => {
        //Input
        const data: ISearch = {
            userToken: "user@clm.com",
            tenantUniqueName: "II",
            sortBy: "name",
        };

        //Our desired output
        const output =
        {
            results: [],
            allResults: 2,
            page: 1,
            pageSize: 100
        }

        //Prepare the response we want to get from axios
        const mockedResponse: AxiosResponse = {
            data: output,
            status: 200,
            statusText: 'OK',
            headers: {},
            config: {},
        };
        // Make the mock return the custom axios response
        mockedAxios.post.mockResolvedValueOnce(mockedResponse);
        expect(axios.post).not.toHaveBeenCalled();
        const res = await SearchDataService.create(data);
        expect(axios.post).toHaveBeenCalled();
        expect(res).toEqual(output);
    });
});