export interface PageResponseModel<T> {
    content: T[],
    empty: boolean
    first: boolean
    last: boolean
    number: number
    numberOfElements: number
    size: number
    totalElements: number
    totalPages: number
}

export class MediaCreateModel {
    constructor(
        public itemId: number,
        public itemCategory: string,
        public imageUrl: string,
        public imageDeleteUrl: string,
        public isThumbnail: boolean,
        public created?: string,
        public createdBy?: string
    ) {}
}

export class MediaStoreModel {
    constructor(
        public id: number,
        public itemId: number,
        public itemCategory: string,
        public imageUrl: string,
        public imageDeleteUrl: string,
        public isThumbnail: boolean,
        public created?: string,
        public createdBy?: string
    ) {}
}