export enum GuideType {
    SYSTEM = 'SYSTEM',
    EXTERNAL = 'EXTERNAL'
}

export enum ObjectType {
    SPOT = 'SPOT',
    EVENT = 'EVENT',
    USER = 'USER',
    GUIDE_THUMBNAIL = 'GUIDE_THUMBNAIL',
    SECTION_GUIDE = 'SECTION_GUIDE',
    ORGANISER = 'ORGANISER'
}

export enum RequestType {
    ADD = 'ADD',
    UPDATE = 'UPDATE',
    REMOVE = 'REMOVE',
    OTHER = 'OTHER'
}

export enum RequestStatus {
    PENDING = 'PENDING',
    APPROVED = 'APPROVED',
    REJECTED = 'REJECTED'
}