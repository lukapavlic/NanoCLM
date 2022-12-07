import ITenant from "./tenant.type";

interface ITenantPost {
    userToken: string;
    tenant: ITenant;
}

interface ITenantRevokeAllow {
    userToken: string;
    tenantUniqueName: string;
    userName: string;
}

export type { ITenantPost, ITenantRevokeAllow };