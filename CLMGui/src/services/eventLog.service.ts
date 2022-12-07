import http from "../http-common";
import { IEventLogResponseEventId, IEventLogResUsrEveTypeCont } from "../types/eventLog/eventLogResponse.type";

class EventLogDataService {
    async getByEventId(userToken: string, tenantUniqueName: string, eventId: string): Promise<IEventLogResponseEventId> {
        const response = await http.get(`/eventlog/${eventId}`, { headers: { userToken: userToken, tenantUniqueName: tenantUniqueName } });
        return response.data;
    }

    async getByUser(userToken: string, tenantUniqueName: string): Promise<Array<IEventLogResUsrEveTypeCont>> {
        const response = await http.get(`/eventlog/user`, { headers: { userToken: userToken, tenantUniqueName: tenantUniqueName } });
        return response.data;
    }

    async getByEventType(userToken: string, tenantUniqueName: string, eventType: string): Promise<Array<IEventLogResUsrEveTypeCont>> {
        const response = await http.get(`/eventlog/eventtype/${eventType}`, { headers: { userToken: userToken, tenantUniqueName: tenantUniqueName } });
        return response.data;
    }

    async getByContactUniqueId(userToken: string, tenantUniqueName: string, contactUniqueId: string): Promise<Array<IEventLogResUsrEveTypeCont>> {
        const response = await http.get(`/eventlog/contact/${contactUniqueId}`, { headers: { userToken: userToken, tenantUniqueName: tenantUniqueName } });
        return response.data;
    }
}

export default new EventLogDataService();