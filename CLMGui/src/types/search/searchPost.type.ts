import IFilter from "../filter/filter.type";

export default interface ISearch {
    userToken: string;
    tenantUniqueName: string;
    page?: number | null;
    pageSize?: number | null;
    searchString?: string | null;
    filters?: Array<IFilter> | null;
    sortBy: string;
    sortOrientation?: string | null;
}