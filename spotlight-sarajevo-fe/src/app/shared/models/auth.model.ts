import { EventCategoryModel, SpotCategoryModel } from "./category.model";

export class LoginModel {
    constructor(
        public email: String,
        public password: String
    ){}
}

export class SystemUserModel {
    constructor(
        public firstName: String,
        public lastName: String,
        public email: String,
        public password: String
    ){}
}

export class PreferencesModel {
    constructor(
        public favouriteSpotCategories: number[],
        public favouriteEventCategories: number[],
        public questionOne: Boolean,
        public questionTwo: Boolean,
        public questionThree: Boolean,
        public questionFour: Boolean,
    ) {}
}

export class LoginResponse {
    constructor(
        public token: String,
        public user: LoggedUserModel
    ) {}
}

export class LoggedUserModel {
    constructor(
        public id: number,
        public firstName: String,
        public lastName: String,
        public email: String,
        public username: String,
        public isAdmin: boolean,
        public isPremium: boolean
    ) {}
}

export class UserInfoModel {
    constructor(
        public id: number,
        public firstName: String,
        public lastName: String,
        public email: String,
        public username: String,
        public isAdmin: boolean,
        public isPremium: boolean,
        public favoriteSpotCategories: SpotCategoryModel[],
        public favoriteEventCategories: EventCategoryModel[],
        public attendedEventsCount: number,
        public visitedSpotsCount: number,
    ) {}
}  