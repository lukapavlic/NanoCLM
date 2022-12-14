export default interface IContact {
    title: string;
    props: {[key: string]: string};
    attrs: Array<string>;
    comments: {[key: string]: string};
}