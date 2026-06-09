export class SpotCategoryModel{
    constructor(
        public id: number,
        public spotCategoryNameBs: string,
        public spotCategoryNameEn: string,
        public spotCategoryDescriptionBs: string,
        public spotCategoryDescriptionEn: string,
        public spotCategoryColorCode: string
    ){}
}

export class EventCategoryModel{
    constructor(
        public id: number,
        public eventCategoryNameBs: string,
        public eventCategoryNameEn: string,
        public eventCategoryDescriptionBs: string,
        public eventCategoryDescriptionEn: string,
        public eventCategoryColorCode: string
    ){}
}

export class GuideCategoryModel {
    constructor(
        public id: number,
        public categoryNameBs: string,
        public categoryNameEn: string,
        public categoryDescriptionBs: string,
        public categoryDescriptionEn: string,
    ){}
}

export class TagModel{
    constructor(
        public id: number,
        public tagNameBs: string,
        public tagNameEn: string,
        public tagDescriptionBs: string,
        public tagDescriptionEn: string
    ){}
}
