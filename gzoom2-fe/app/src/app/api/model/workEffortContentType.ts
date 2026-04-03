/**
 * Model of a WorkEffortContentType.
 */
export class WorkEffortContentType {
    constructor(
        public workEffortContentTypeId?: string,
        public parentTypeId?: string,
        public description?: string,
        public descriptionLang?: string,
        public contentTypeId?: string
    ) { }
}
