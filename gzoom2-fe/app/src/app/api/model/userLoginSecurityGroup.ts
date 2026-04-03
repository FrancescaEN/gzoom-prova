export class UserLoginSecurityGroup {
    constructor(
        public groupId: string,
        public userLoginId: string,
        public fromDate: Date,
        public thruDate?: Date
    ) { }
}