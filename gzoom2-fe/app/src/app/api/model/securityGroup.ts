export class SecurityGroup {
    constructor(
        public groupId: string,
        public description: string,
        public defaultPortalPageId?: string
    ) { }
}