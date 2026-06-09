import { TagModel } from './category.model';
import { MediaCreateModel, MediaStoreModel } from './shared.model';

export class EventShorthandModel {
  constructor(
    public id: number,
    public slug: string,
    public officialNameBs: string,
    public officialNameEn: string,
    public smallDescriptionBs: string,
    public smallDescriptionEn: string,
    public categoryNameBs: string,
    public categoryNameEn: string,
    public eventTags: TagModel[],
    public startDate: string,
    public endDate: string,
    public thumbnailImage: MediaStoreModel,
  ) {}
}

export class EventOverviewModel {
  constructor(
    public id: number,
    public slug: string,
    public officialNameBs: string,
    public officialNameEn: string,
    public smallDescriptionBs: string,
    public smallDescriptionEn: string,
    public fullDescriptionBs: string,
    public fullDescriptionEn: string,
    public eventLat: number,
    public eventLon: number,
    public location: string,
    public locationLinkSlug: string,
    public categoryId: number,
    public categoryNameEn: String,
    public categoryNameBs: String,
    public eventTags: TagModel[],
    public startDate: string,
    public endDate: string,
    public entryPrice: number,
    public cancelRefund: boolean,
    public eventLanguage: string,
    public ageLimit: number,
    public reservation: boolean,
    public organiser: EventOrganiserModel,
    public thumbnailImage: MediaStoreModel,
    public images: MediaStoreModel[],
  ) {}
}

export class EventUpdateModel {
  constructor(
    public id: number,
    public slug: string,
    public officialNameBs: string,
    public officialNameEn: string,
    public smallDescriptionBs: string,
    public smallDescriptionEn: string,
    public fullDescriptionBs: string,
    public fullDescriptionEn: string,
    public eventLat: number,
    public eventLon: number,
    public location: string,
    public locationLinkSlug: string,
    public categoryId: number,
    public eventTagIds: number[],
    public startDate: string,
    public endDate: string,
    public entryPrice: number,
    public cancelRefund: boolean,
    public eventLanguage: string,
    public ageLimit: number,
    public reservation: boolean,
    public thumbnailImage: string,
    public newThumbnailImage: MediaCreateModel | null,
    public toAddImages: MediaCreateModel[],
    public toRemoveImages: number[],
  ) {}
}

export class EventCreateModel {
  constructor(
    public slug: string,
    public officialNameBs: string,
    public officialNameEn: string,
    public smallDescriptionBs: string,
    public smallDescriptionEn: string,
    public fullDescriptionBs: string,
    public fullDescriptionEn: string,
    public eventLat: number,
    public eventLon: number,
    public location: string,
    public locationLinkSlug: string,
    public categoryId: number,
    public eventTagIds: number[],
    public startDate: string,
    public endDate: string,
    public entryPrice: number,
    public cancelRefund: boolean,
    public eventLanguage: string,
    public ageLimit: number,
    public reservation: boolean,
    public organiserId: number,
    public newThumbnailImage: MediaCreateModel | null,
    public toAddImages: MediaCreateModel[],
  ) {}
}

export class EventOrganiserModel {
  constructor(
    public id: number,
    public organiserName: string,
    public organiserCreationDate: string,
    public organiserCategoryId: number,
    public organiserCategoryNameBs: string,
    public organiserCategoryNameEn: string,
    public organiserPhone: string,
    public organiserEmail: string,
    public organiserWebsite: string,
    public thumbnailImage: MediaStoreModel,
    public overallAtmosphere: number,
    public overallQuality: number,
    public overallEnjoyability: number,
    public overallRating: number,
  ) {}
}

export class EventOrganiserCreateModel {
  constructor(
    public organiserCategoryId: number,
    public organiserCreationDate: string,
    public organiserEmail: string,
    public organiserName: string,
    public organiserPhone: string,
    public organiserWebsite: string,
    public newThumbnailImage: MediaCreateModel,
  ) {}
}

export class EventOrganiserUpdateModel {
  constructor(
    public id: number,
    public organiserCategoryId: number,
    public organiserCreationDate: string,
    public organiserEmail: string,
    public organiserName: string,
    public organiserPhone: string,
    public organiserWebsite: string,
    public existingThumbnailImageUrl: string,
    public newThumbnailImage: MediaCreateModel
  ) {}
}

export class EventOrganiserReviewModel {
  constructor(
    public id: number,
    public organiserId: number,
    public userId: number,
    public username: number,
    public created: string,
    public modified: string,
    public header: string,
    public body: string,
    public userOverallRating: number,
    public userOrganiserQuality: number,
    public userOrganiserAtmosphere: number,
    public userOrganiserEnjoyability: number,
  ) {}
}

export class EventOrganiserReviewCreateModel {
  constructor(
    public organiserId: number,
    public header: string,
    public body: string,
    public userOverallRating: number,
    public userOrganiserQuality: number,
    public userOrganiserAtmosphere: number,
    public userOrganiserEnjoyability: number,
  ) {}
}

export class EventOrganiserReviewUpdateModel {
  constructor(
    public id: number,
    public organiserId: number,
    public userId: number,
    public header: string,
    public body: string,
    public userOverallRating: number,
    public userOrganiserQuality: number,
    public userOrganiserAtmosphere: number,
    public userOrganiserEnjoyability: number,
  ) {}
}

export interface EventDateCheckModel {
  [date: string]: boolean;
}
