export class SecurityGroupContent {
    constructor(
        public groupId: string,
        public contentId: string,
        public fromDate: Date,
        public thruDate?: Date
    ) { }
}