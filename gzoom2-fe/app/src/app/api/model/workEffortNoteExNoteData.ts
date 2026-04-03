import { NoteData } from "./noteData";
import { WorkEffortNote } from "./workEffortNote";

/**
 * Model of a WorkEffortNoteExNoteData.
 */
export class WorkEffortNoteExNoteData extends WorkEffortNote {
    public noteData?: NoteData;

    constructor(
        public workEffortId?: string,
        public noteId?: string,
        public internalNote?: string,
        public isMain?: string,
        public isHtml?: string,
        public sequenceId?: number,
        public isPosted?: string,

        public noteInfo?: string,
        public noteInfoLang?: string,
        public noteName?: string,
        public noteNameLang?: string,
        public noteParty?: string,
        public noteDateTime?: Date,
    ) {
        super(workEffortId, noteId, internalNote, isMain, isHtml, sequenceId, isPosted);

        this.noteData = new NoteData(noteId, noteInfo, noteInfoLang, noteName, noteNameLang, noteParty, noteDateTime);


    }
}
