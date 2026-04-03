/**
 * Model of a WorkEffortNote.
 */
export class WorkEffortNote {
    constructor(
        public workEffortId?: string,
        public noteId?: string,
        public internalNote?: string,
        public isMain?: string,
        public isHtml?: string,
        public sequenceId?: number,
        public isPosted?: string
    ) { }
}
