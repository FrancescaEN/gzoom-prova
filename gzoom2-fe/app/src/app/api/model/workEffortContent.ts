/**
 * Model of WorkEffortContent
 */
export class WorkEffortContent {
    constructor(
        public workEffortId?: string,
        public contentId?: string,
        public workEffortContentTypeId?: string,
        public fromDate?: Date,
        public thruDate?: Date
    ) { }
}