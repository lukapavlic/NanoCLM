export default interface IContactResponse {
    mongoId: string;
    uniqueId: string;
    title: string;
    props: Map<string, string>;
    attrs: Array<string>;
    comments: Map<string, string>;
}