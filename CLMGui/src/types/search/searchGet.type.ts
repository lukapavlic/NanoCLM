export default interface ISearchGet {
    userToken: string;
    tenantUniqueName: string;
    page?: number | null;
    pageSize?: number | null;
    searchId: string;
}