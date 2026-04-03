import { VariableGridArray } from "app/layout/tables/table-editing-cell/table-editing-cell-configuration";

/**
 * Model of a NoteData.
 */
export class NoteData {
    constructor(public noteId?: string,
        public noteInfo?: string,
        public noteInfoLang?: string,
        public noteName?: string,
        public noteNameLang?: string,
        public noteParty?: string,
        public noteDateTime?: Date,
    ) { }
}
