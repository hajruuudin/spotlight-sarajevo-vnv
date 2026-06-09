import { EventShorthandModel } from "./event.model";
import { SpotShorthandModel } from "./spot.model";

export class CollectionModel {
  constructor(
    public id: number,
    public collectionName: string,
    public collectionDescription: string,
    public collectionType: 'SPOT' | 'EVENT' | string,
    public created: string,
    public createdBy: string,
    public isSystem: boolean,
    public userId: number
  ) {}
}

export class CollectionItemsModel {
  constructor(
    public collectionId: number,
    public collectionName: string,
    public collectionDescription: string,
    public collectionType: 'SPOT' | 'EVENT',
    public isSystem: boolean,
    public collectionItems: (SpotShorthandModel | EventShorthandModel)[]
  ) {}
}

export class CollectionCreateModel {
  constructor(
    public collectionName: string,
    public collectionDescription: string,
    public collectionType: 'SPOT' | 'EVENT' | string
  ) {}
}

export class CollectionUpdateModel {
  constructor(
    public id: number,
    public collectionName: string,
    public collectionDescription: string
  ) {}
}

export class CollectionAddItemModel {
  constructor(
    public objectId: number,
    public objectType: string,
    public collectionId: number,
    public isSystem: boolean
  ) {}
}

export class CollectionSpotModel implements CollectionItem {
  constructor(
    public id: number,
    public collectionId: number,
    public spotId: number
  ) {}
}

export class CollectionEventModel implements CollectionItem{
  constructor(
    public id: number,
    public collectionId: number,
    public eventId: number
  ) {}
}

export class AddCollectionItemsModel{
  constructor(
    public ids: number[],
    public collectionType: string,
    public objectId: number,
    public isSystem: boolean = false
  ){}
}

export class SpotInCollectionsModel implements CollectionItems{
    constructor(
        public ids: number[]
    ){}
}


export class EventInCollectionModel implements CollectionItems{
    constructor(
        public ids: number[]
    ){}
}

export interface CollectionItems{}
export interface CollectionItem{}