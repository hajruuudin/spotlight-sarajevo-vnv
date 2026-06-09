import { GuideType } from "../constants/ObjectTypes";
import { MediaCreateModel, MediaStoreModel } from './shared.model';

export class TouristGuideModel {
  constructor(
    public id: number,
    public slug: string,
    public guideTitleBs: string,
    public guideTitleEn: string,
    public guideSmallDescriptionBs: string,
    public guideSmallDescriptionEn: string,
    public guideFullDescriptionBs: string,
    public guideFullDescriptionEn: string,
    public guideType: GuideType,
    public contactInfo: { [key: string]: string } | null,
    public categoryId: number,
  ) {}
}

export class TouristGuideShorthandModel {
  constructor(
    public id: number,
    public slug: string,
    public guideTitleBs: string,
    public guideTitleEn: string,
    public guideSmallDescriptionBs: string,
    public guideSmallDescriptionEn: string,
    public thumbnailImage: MediaStoreModel,
    public categoryNameBs: string,
    public categoryNameEn: string
  ) {}
}

export class TouristGuideSectionModel {
  constructor(
    public id: number,
    public guideId: number,
    public sectionTitleBs: string,
    public sectionTitleEn: string,
    public sectionBodyBs: string,
    public sectionBodyEn: string,
    public thumbnailImage: MediaStoreModel
  ) {}
}

export class TouristGuideOverviewModel {
  constructor(
    public id: number,
    public slug: string,
    public guideTitleBs: string,
    public guideTitleEn: string,
    public guideSmallDescriptionBs: string,
    public guideSmallDescriptionEn: string,
    public guideFullDescriptionBs: string,
    public guideFullDescriptionEn: string,
    public categoryNameBs: string,
    public categoryNameEn: string,
    public sections: TouristGuideSectionModel[],
    public guideType: GuideType,
    public contactInfo: { [key: string]: string },
    public thumbnailImage: MediaStoreModel,
    public categoryId: number
  ) {}
}

export class TouristGuideSectionUpdateModel {
  constructor(
    public id: number,
    public sectionTitleBs: string,
    public sectionTitleEn: string,
    public sectionBodyBs: string,
    public sectionBodyEn: string,
    public thumbnailUrl: string,
    public newThumbnailImage: MediaCreateModel | null,
    public orderIdx: number
  ) {}
}

export class TouristGuideUpdateModel {
  constructor(
    public id: number,
    public slug: string,
    public guideTitleBs: string,
    public guideTitleEn: string,
    public guideSmallDescriptionBs: string,
    public guideSmallDescriptionEn: string,
    public guideFullDescriptionBs: string,
    public guideFullDescriptionEn: string,
    public categoryId: number,
    public thumbnailUrl: string,
    public newThumbnailImage: MediaCreateModel | null,
    public toUpdateSections: TouristGuideSectionUpdateModel[],
    public toAddSections: TouristGuideSectionCreateModel[],
    public toDeleteSections: number[]
  ) {}
}

export class TouristGuideSectionCreateModel {
  constructor(
    public sectionTitleBs: string,
    public sectionTitleEn: string,
    public sectionBodyBs: string,
    public sectionBodyEn: string,
    public thumbnailImage: string,
    public orderIdx: number
  ) {}
}

export class TouristGuideCreateModel {
  constructor(
    public guideTitleBs: string,
    public guideTitleEn: string,
    public guideSmallDescriptionBs: string,
    public guideSmallDescriptionEn: string,
    public guideFullDescriptionBs: string,
    public guideFullDescriptionEn: string,
    public sections: TouristGuideSectionCreateModel[],
    public guideType: GuideType,
    public contactInfo: { [key: string]: string },
    public newThumbnailImage: MediaCreateModel | null,
    public categoryId: number
  ) {}
}