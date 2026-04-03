/**
 * Model of a WorkEffortAssoc.
 */
export class WorkEffortAssoc {
    constructor(
        public workEffortIdFrom?: string,
        public workEffortIdTo?: string,
        public workEffortAssocTypeId?: string,
        public sequenceNum?: string,
        public fromDate?: Date,
        public thruDate?: Date,
        public assocWeight?: number,
        public comments?: string,
        public commentsLang?: string,
        public weMeasureEvalId?: string,
    ) { }
}