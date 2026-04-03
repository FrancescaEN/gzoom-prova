export class QrtzJobDetails {
    constructor(
        public jobName?: string,
        public jobClassName?: string,
        public jobData?: Blob,
        public description?: string,
    ) { }

}