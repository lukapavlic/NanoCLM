import IContact from "./contact.type";

interface IContactPost {
    userToken: string;
    tenantUniqueName: string;
    contact: IContact;
}

interface IContactPostProps {
    userToken: string;
    tenantUniqueName: string;
    id: string;
    name: string;
    value: string;
}

interface IContactPostCommentsAttrs {
    userToken: string;
    tenantUniqueName: string;
    id: string;
    value: string;
}

export type { IContactPost, IContactPostProps, IContactPostCommentsAttrs };