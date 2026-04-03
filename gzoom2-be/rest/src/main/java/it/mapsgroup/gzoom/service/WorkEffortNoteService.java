package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Messages;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dao.NoteDataDao;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortDao;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortNoteDao;
import it.mapsgroup.gzoom.mybatis.dao.WorkEffortTypeAttrDao;
import it.mapsgroup.gzoom.mybatis.dto.NoteData;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortNote;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortNoteExNoteData;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortTypeAttr;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.mapsgroup.gzoom.security.Principals.principal;

@Service
public class WorkEffortNoteService {
    private final Configuration config;
    private final WorkEffortNoteDao workEffortNoteDao;
    private final NoteDataDao noteDataDao;
    private final WorkEffortDao workEffortDao;
    private final WorkEffortTypeAttrDao workEffortTypeAttrDao;

    @Autowired
    public WorkEffortNoteService(Configuration config, WorkEffortNoteDao workEffortNoteDao, NoteDataDao noteDataDao, WorkEffortDao workEffortDao, WorkEffortTypeAttrDao workEffortTypeAttrDao) {
        this.config = config;
        this.workEffortNoteDao = workEffortNoteDao;
        this.noteDataDao = noteDataDao;
        this.workEffortDao = workEffortDao;
        this.workEffortTypeAttrDao = workEffortTypeAttrDao;
    }


    public int getTotale(InfoPage infoPage) {
        return this.workEffortNoteDao.getTotale(infoPage);
    }
    public Result<WorkEffortNoteExNoteData> getWorkEffortNoteExNoteDataListPagination(InfoPage infoPage) {
        List<WorkEffortNoteExNoteData> list = this.workEffortNoteDao.getWorkEffortNoteExNoteDataList(infoPage);
        return new Result<>(list, list.size());
    }

    public WorkEffortNoteExNoteData createWorkEffortNoteExNoteData(WorkEffortNoteExNoteData req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT_NOTE_EX_NOTE_DATA, Messages.IS_REQUIRED) );
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getInternalNote(), msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.INTERNAL_NOTE, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getIsMain(), msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.IS_MAIN, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getIsHtml(), msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.IS_HTML, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getSequenceId(), msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.SEQUENCE_ID, Messages.IS_REQUIRED));

        Validators.assertNotBlank(req.getNoteData().getNoteName(), msg.getMessageColumn(Messages.NOTE_DATA, Messages.NOTE_NAME, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getNoteData().getNoteDateTime(), msg.getMessageColumn(Messages.NOTE_DATA, Messages.NOTE_DATE_TIME, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getNoteData().getNoteInfo(), msg.getMessageColumn(Messages.NOTE_DATA, Messages.NOTE_INFO, Messages.IS_REQUIRED));

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getNoteData().getNoteInfoLang(), msg.getMessageColumn(Messages.NOTE_DATA, Messages.NOTE_INFO_LANG, Messages.IS_REQUIRED));
        }

        WorkEffort workEffort = this.workEffortDao.getWorkEffort(req.getWorkEffortId());
        WorkEffortTypeAttr workEffortTypeAttr = this.workEffortTypeAttrDao.get(workEffort.getWorkEffortTypeId(), req.getNoteData().getNoteName());
        req.getNoteData().setNoteNameLang(workEffortTypeAttr.getAttrNameLang());

        NoteData noteData = req.getNoteData();
        String newNoteId = this.noteDataDao.create(noteData,  principal().getUserLoginId());

        if (newNoteId != null){
            WorkEffortNote workEffortNote = new WorkEffortNote(req.getWorkEffortId(), newNoteId, req.getInternalNote(), req.getIsMain(), req.getIsHtml(), req.getSequenceId());

            this.workEffortNoteDao.create(workEffortNote,  principal().getUserLoginId());
            return this.workEffortNoteDao.getWorkEffortNoteExNoteData(workEffortNote.getWorkEffortId(), workEffortNote.getNoteId(), noteData.getNoteName());
        }

        return null;

    }

    public boolean updateWorkEffortNoteExNoteData(WorkEffortNoteExNoteData req) {
        Messages msg = new Messages();
        Validators.assertNotNull(req, msg.getMessageTable(Messages.WORK_EFFORT_NOTE_EX_NOTE_DATA, Messages.IS_REQUIRED) );
        Validators.assertNotBlank(req.getWorkEffortId(), msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getInternalNote(), msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.INTERNAL_NOTE, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getIsMain(), msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.IS_MAIN, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getIsHtml(), msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.IS_HTML, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getSequenceId(), msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.SEQUENCE_ID, Messages.IS_REQUIRED));

        Validators.assertNotBlank(req.getNoteData().getNoteName(), msg.getMessageColumn(Messages.NOTE_DATA, Messages.NOTE_NAME, Messages.IS_REQUIRED));
        Validators.assertNotNull(req.getNoteData().getNoteDateTime(), msg.getMessageColumn(Messages.NOTE_DATA, Messages.NOTE_DATE_TIME, Messages.IS_REQUIRED));
        Validators.assertNotBlank(req.getNoteData().getNoteInfo(), msg.getMessageColumn(Messages.NOTE_DATA, Messages.NOTE_INFO, Messages.IS_REQUIRED));

        if (config.getLanguageType().equals("BILING")) {
            Validators.assertNotBlank(req.getNoteData().getNoteInfoLang(), msg.getMessageColumn(Messages.NOTE_DATA, Messages.NOTE_INFO_LANG, Messages.IS_REQUIRED));
        }

        WorkEffort workEffort = this.workEffortDao.getWorkEffort(req.getWorkEffortId());
        WorkEffortTypeAttr workEffortTypeAttr = this.workEffortTypeAttrDao.get(workEffort.getWorkEffortTypeId(), req.getNoteData().getNoteName());

        req.getNoteData().setNoteNameLang(workEffortTypeAttr.getAttrNameLang());

        NoteData noteData = req.getNoteData();

        this.noteDataDao.update(noteData, principal().getUserLoginId());

        WorkEffortNote workEffortNote = new WorkEffortNote(req.getWorkEffortId(), noteData.getNoteId(), req.getInternalNote(), req.getIsMain(), req.getIsHtml(), req.getSequenceId());
        return this.workEffortNoteDao.update(workEffortNote,  principal().getUserLoginId());
    }

    public boolean deleteWorkEffortNoteExNoteData(String workEffortId, String noteId) {
        Messages msg = new Messages();
        Validators.assertNotBlank(workEffortId, msg.getMessageColumn(Messages.WORK_EFFORT_NOTE, Messages.WORK_EFFORT_ID, Messages.IS_REQUIRED));
        Validators.assertNotBlank(noteId, msg.getMessageColumn(Messages.NOTE_DATA, Messages.NOTE_ID, Messages.IS_REQUIRED));
        WorkEffortNote workEffortNote = this.workEffortNoteDao.getWorkEffortNote(workEffortId, noteId);
        Validators.assertNotNull(workEffortNote, msg.getMessagesWithSpace(Messages.INVALID, Messages.WORK_EFFORT_NOTE));
        this.workEffortNoteDao.delete(workEffortId, noteId);
        return this.noteDataDao.delete(noteId);
    }
}
