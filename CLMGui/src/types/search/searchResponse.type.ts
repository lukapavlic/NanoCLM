import IContactResponse from "../contact/contactResponse.type";

export default interface ISearchResponse {
    results: Array<IContactResponse>;
    allResults: number;
    page: number;
    pageSize: number;
}