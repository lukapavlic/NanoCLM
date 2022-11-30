export default interface ITenant {
    title: string;
    uniqueName: string;
    allowedUsers: Array<string>;
}