export default interface ITenantResponse {
    mongoId: string;
    title: string;
    tenantUniqueName: string;
    allowedUsers: Array<string>;
}