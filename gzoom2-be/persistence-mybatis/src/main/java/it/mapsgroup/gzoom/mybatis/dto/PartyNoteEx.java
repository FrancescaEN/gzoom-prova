package it.mapsgroup.gzoom.mybatis.dto;

public class PartyNoteEx extends PartyNote {
    private NoteData noteData;

    public NoteData getNoteData() {
        return noteData;
    }

    public void setNoteData(NoteData noteData) {
        this.noteData = noteData;
    }
}
