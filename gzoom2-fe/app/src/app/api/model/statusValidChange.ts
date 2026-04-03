export class StatusValidChange {
    constructor(
        public statusId: string,
        public statusIdTo: string,
        public transitionName: string,
        public conditionExpression?: string
    ) { }
}