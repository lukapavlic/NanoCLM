import http from "../http-common";
import { IContactsGetAll, IContactsGetById } from "../types/contact/contactGet.type";
import IContactDelete from "../types/contact/contactDelete.type";
import IContactResponse from "../types/contact/contactResponse.type";
import { IContactPost, IContactPostCommentsAttrs, IContactPostProps } from "../types/contact/contactPost.type";

class ContactDataService {
    async getAll(data: IContactsGetAll): Promise<Array<IContactResponse>> {
        const response = await http.get("/contacts", { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }

    async getById(data: IContactsGetById): Promise<IContactResponse> {
        const response = await http.get(`/contacts/${data.id}`, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }

    async delete(data: IContactDelete): Promise<string> {
        const response = await http.delete(`/contacts/${data.id}`, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }

    async post(data: IContactPost): Promise<IContactResponse> {
        const response = await http.post('/contacts', data.contact, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }

    async postProps(data: IContactPostProps): Promise<IContactResponse> {
        const response = await http.post(`/contacts/${data.id}/props`, { name: data.name, value: data.value }, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }

    async postComments(data: IContactPostCommentsAttrs): Promise<IContactResponse> {
        const response = await http.post(`/contacts/${data.id}/comments`, data.value, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }

    async postAttrs(data: IContactPostCommentsAttrs): Promise<IContactResponse> {
        const response = await http.post(`/contacts/${data.id}/attrs`, data.value, { headers: { userToken: data.userToken, tenantUniqueName: data.tenantUniqueName } });
        return response.data;
    }
}

export default new ContactDataService();