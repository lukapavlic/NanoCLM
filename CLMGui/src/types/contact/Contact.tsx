/*export default interface Contact {
    _id: { $oid: string },
    uniqueId: string,
    title: string,
    props: { 
        name: string, address: string, surname: string 
    },
    attrs: Array<string>,
    comments: {},
    _class: string
}*/

    export interface Id {
        $oid: string;
    }

    export interface Props {
        name?: string;
        address?: string;
        surname?: string;
    }

    export interface Comments {
    }

    export default interface Contact {
        _id: Id;
        uniqueId: string;
        title: string;
        props: Props;
        attrs: any[];
        comments: Comments;
        _class: string;
    }
