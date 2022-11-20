import IFilter from "../filter/filter.type";

export default interface ISearchBody {
    searchString?: string | null;
    filters?: IFilter[] | null;
    sortBy: string;
    sortOrientation?: string | null;
}