package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.mybatis.dto.NoteData;
import it.mapsgroup.gzoom.mybatis.mapper.NoteDataMapper;
import it.mapsgroup.gzoom.persistence.common.SequenceGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class NoteDataDao extends AbstractDao {
    private static final Logger LOG = getLogger(NoteDataDao.class);
    private final NoteDataMapper noteDataMapper;
    private final SequenceGenerator sequenceGenerator;

    @Autowired
    public NoteDataDao(NoteDataMapper noteDataMapper, SequenceGenerator sequenceGenerator) {
        this.noteDataMapper = noteDataMapper;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Transactional
    public List<NoteData> getNoteDataParamsTimesheet(String userLoginId) {
        LOG.info("find note data by id");
        List<NoteData> noteDataList = this.noteDataMapper.getNoteDataParamsTimesheet(userLoginId);
        LOG.info("size = {}", noteDataList.size());
        return noteDataList;
    }

    @Transactional
    public NoteData getNoteDataByPrefValue(String prefValue) {
        LOG.info("find note data by prefValue");
        NoteData noteData = this.noteDataMapper.getNoteDataByPrefValue(prefValue);
        LOG.info("size = {}", noteData);
        return noteData;
    }

    @Transactional
    public boolean update(NoteData noteData, String userLoginId) {
        LOG.info("update noteData");
        setUpdateTimestamp(noteData);
        noteData.setLastModifiedByUserLogin(userLoginId);
        int result = this.noteDataMapper.updateByPrimaryKey(noteData);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String id) {
        LOG.info("delete noteData");
        int result = this.noteDataMapper.deleteByPrimaryKey(id);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public String create(NoteData noteData, String userLoginId) {
        LOG.info("create noteData");
        String newId = this.sequenceGenerator.getNextSeqId("NoteData");
        noteData.setNoteId(newId);
        setCreatedTimestamp(noteData);
        noteData.setCreatedByUserLogin(userLoginId);
        int result = this.noteDataMapper.insert(noteData);
        LOG.info("result = {}", result);
        return (result > 0)? newId : null;
    }
}
