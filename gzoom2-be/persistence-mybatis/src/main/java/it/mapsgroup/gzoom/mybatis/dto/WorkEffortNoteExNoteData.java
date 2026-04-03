package it.mapsgroup.gzoom.mybatis.dto;

public class WorkEffortNoteExNoteData extends WorkEffortNote {

    private NoteData noteData;
    private int totalRow;

    private WorkEffortView workEffortView;

    public WorkEffortView getWorkEffortView() {
        return workEffortView;
    }

    public void setWorkEffortView(WorkEffortView workEffortView) {
        this.workEffortView = workEffortView;
    }

    public NoteData getNoteData() {
        return noteData;
    }

    public void setNoteData(NoteData noteData) {
        this.noteData = noteData;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getTotalRow() {
        return totalRow;
    }
}
