/**
 * Model of Content
 */
export class Content {
    constructor(
        public contentId?: string,
        public contentTypeId?: string,
        public statusId?: string,
        public description?: string,
        public descriptionLang?: string,
        public contentName?: string,
        public mimeTypeId?: string,
        public objectInfo?: string,
    ) { }
}