import IFilter from "../filter/filter.type";

export default interface ISearchMgmtResponse {
    mongoId: string;
    searchName: string;
    publicSearchOnTenant: boolean;
    search: string;
    filters: Array<IFilter>;
    sortBy: string;
    sortOrientation: string;
    presentation: string;
}