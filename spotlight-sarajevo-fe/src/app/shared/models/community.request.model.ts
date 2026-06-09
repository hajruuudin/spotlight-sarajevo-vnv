import { ObjectType, RequestStatus, RequestType } from "../constants/ObjectTypes";

export class CommunityRequestCreateModel {
    constructor(
        public requestType: RequestType,
        public objectType: ObjectType,
        public requestHeader: string,
        public requestDescription: string,
        public requestBody?: any
    ) {}
}

export class CommunityRequestDataModel {
    constructor(
        public id: number,
        public communityRequestId: number,
        public pendingInfo: any
    ) {}
}

export class CommunityRequestModel {
    constructor(
        public id: number,
        public userId: number,
        public username: string,
        public requestType: RequestType,
        public objectType: ObjectType,
        public requestHeader: string,
        public requestDescription: string,
        public status: RequestStatus,
        public createdAt: Date
    ) {}
}

export class CommunityRequestOverviewModel {
    constructor(
        public id: number,
        public userId: number,
        public username: string,
        public requestType: RequestType,
        public objectType: ObjectType,
        public requestHeader: string,
        public requestDescription: string,
        public status: RequestStatus,
        public pendingInfo: any,
        public createdAt: Date
    ){}
}

export class CommunityRequestStatusUpdate {
    constructor(
        public requestId: number,
        public status: RequestStatus,
        public statusInfo?: string
    ) {}
}

export class UserCommunityRequestModel {
    constructor(
        public id: number,
        public userId: number,
        public username: string,
        public requestType: RequestType,
        public objectType: ObjectType,
        public requestHeader: string,
        public requestDescription: string,
        public status: RequestStatus,
        public createdAt: Date
    ) {}
}