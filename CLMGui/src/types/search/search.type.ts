import ISearchBody from "./searchBody.type";
import ISearchHeaders from "./searchHeaders.type";
import ISearchParams from "./searchParams.type";

export default interface ISearch {
    headers: ISearchHeaders;
    params?: ISearchParams | null;
    body: ISearchBody;
}