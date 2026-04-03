export class StatusItem {
    constructor(
        public statusId: string,
        public statusTypeId: string,
        public description: string,
        public descriptionLang: string,
        public statusCode: string,
        public sequenceId: string,
        public actStEnumId: string
    ) { }
}