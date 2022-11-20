import { FilterType } from "./filterType.type";

export default interface IFilter {
    type?: FilterType | null;
    propertyName?: string | null;
    content?: string | null;
}