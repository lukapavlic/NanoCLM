interface IEventLogResponseEventId {
    mongoId: string;
    user: string;
    tenantUniqueName: string;
    contactUniqueId: string;
    eventType: string;
    timestamp: string;
    oldContent: string;
    relatedContent: string;
    newContent: string;
}

interface IEventLogResUsrEveTypeCont {
    eventId: string;
    user: string;
    contactUniqueId: string;
    eventType: string;
    timestamp: string;
    relatedContent: string;
}



export type { IEventLogResponseEventId, IEventLogResUsrEveTypeCont };