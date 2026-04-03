export class UomRangeValues {
    constructor(
        public uomRangeId: string,
        public uomRangeValuesId: string,
        public comments?: string,
        public isPositive?: string,
        public fromValue?: number,
        public thruValue?: number,
        public iconContentId?: string,
        public alert?: string,
        public rangeValuesFactor?: number,
        public rangeValuesFactorMin?: number,
        public colorEnumId?: string,
        public prorateRange?: string,
        public commentsLang?: string
    ) { }
}