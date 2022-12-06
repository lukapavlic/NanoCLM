export default interface ITenantRes {
    mongoId: string;
    title: string;
    tenantUniqueName: string;
    allowedUsers: Array<string>;
}