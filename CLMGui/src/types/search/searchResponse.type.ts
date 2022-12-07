import IContactResponse from "../contact/contactResponse.type";

export default interface ISearchResponse {
    results: IContactResponse;
    allResults: number;
    page: number;
    pageSize: number;
}