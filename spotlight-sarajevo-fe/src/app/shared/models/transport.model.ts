export class TransportMethodModel{
    constructor(
        public id: number,
        public methodNameBs: string,
        public methodNameEn: string,
        public methodDescriptionBs: string,
        public methodDescriptionEn: string,       
        public geometryGeoJson: string
    ){}
}

export class TransportMethodShorthandModel{
    constructor(
        public id: number,
        public methodNameBs: string,
        public methodNameEn: string,
    ){}
}

export class TransportMethodLineModel{
    constructor(
        public id: number,
        public operatorId: number,
        public transportOperatorName: string,
        public lineStart: string,
        public lineEnd: string,
        public lineNumber: string,
        public geometryGeoJson: string
    ){}
}

export class TaxiOperatorModel{
    constructor(
        public id: number,
        public company: string,
        public phone: string,
        public website?: string
    ){}
}
