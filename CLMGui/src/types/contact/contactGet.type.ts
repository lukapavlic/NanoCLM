interface IContactsGetAll {
    userToken: string;
    tenantUniqueName: string;
}

interface IContactsGetById {
    userToken: string;
    tenantUniqueName: string;
    id: string;
}

export type { IContactsGetAll, IContactsGetById };
