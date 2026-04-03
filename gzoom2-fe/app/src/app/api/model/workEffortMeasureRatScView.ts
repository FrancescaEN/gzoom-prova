/**
 * Model of WorkEffortMeasureRatScView
 */
export class WorkEffortMeasureRatScView {
    constructor(
        public workEffortMeasureId?: string,
        public uomId?: string,
        public uomRatingValue?: number,
        public uomCode?: string,
        public uomCodeLang?: string,
        public uomDescr?: string,
        public uomDescrLang?: string

    ) { }
}