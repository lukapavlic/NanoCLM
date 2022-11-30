import ITenant from "./tenant.type";

export default interface ITenantPost {
    userToken: string;
    tenant: ITenant;
}