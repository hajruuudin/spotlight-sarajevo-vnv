export interface TableColumnDefinitions{}

export interface TableDataModel{}


export class EventTableDataModel implements TableDataModel{
    constructor(
        id: number,
        thumbnail: string,
        slug: string,
        nameBs: string,
        nameEn: string,
        categoryEn: string,
        categoryBs: string,
        rating: number,
        modified: string
    ){}
}

export class GuideTableDataModel implements TableDataModel{
    constructor(
        id: number,
        thumbnail: string,
        slug: string,
        nameBs: string,
        nameEn: string,
        modified: string
    ){}
}

export class PublicTransportTableDataModel implements TableDataModel{
    constructor(
        id: number,
        nameBs: string,
        nameEn: string,
        modified: string
    ){}
}

export class UserTableDataModel implements TableDataModel{
    constructor(
        id: number,
        username: string,
        email: string,
        modified: string
    ){}
}

export class CommunityRequestTableDataModel implements TableDataModel{
    constructor(
        id: number,
        username: string,
        requestHeader: string,
        type: string,
        status: string,
        modified: string
    ){}
}