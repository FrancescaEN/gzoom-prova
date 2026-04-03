/**
 * Model of a WorkEffortPurposeType.
 */
export class WorkEffortPurposeType {
    constructor(
        public workEffortPurposeTypeId?: string,
        public workEffortPurposeTypeCode?: string,
        public purposeTypeEnumId?: string,
        public parentTypeId?: string,
        public descriptionLang?: string,
        public description?: string
    ) { }


}