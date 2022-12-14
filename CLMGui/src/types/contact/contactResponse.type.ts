export default interface IContactResponse {
    mongoId: string;
    uniqueId: string;
    title: string;
    props: {[key: string]: string};
    attrs: Array<string>;
    comments: {[key: string]: string};
}