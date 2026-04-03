package it.mapsgroup.gzoom.mybatis.dao;

import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortNote;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortNoteExNoteData;
import it.mapsgroup.gzoom.mybatis.mapper.WorkEffortNoteMapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorkEffortNoteDao extends AbstractDao {
    private static final Logger LOG = getLogger(WorkEffortNoteDao.class);
    private final WorkEffortNoteMapper workEffortNoteMapper;

    @Autowired
    public WorkEffortNoteDao(WorkEffortNoteMapper workEffortNoteMapper) {
        this.workEffortNoteMapper = workEffortNoteMapper;
    }

    public int getTotale(InfoPage infoPage) {
        if(infoPage.getFilter() != null){
            boolean control = false;
            for(Filter item: infoPage.getFilter()){
                if(item.getValue() != null){
                    control = true;
                }
            }

            if(control){
                for(Filter item: infoPage.getFilter()){
                    if(item.getValue() != null){
                        if(item.getField().equals("noteDateTime")){
                            Instant instant = Instant.parse(item.getValue());
                            item.setDateValue(instant);
                        }
                        if(item.getField().equals("sequenceId") ){
                            item.setBigDecimalValue(new BigDecimal(item.getValue()));
                        }
                        if(item.getField().equals("isHtmlDesc") || item.getField().equals("internalNoteDesc") || item.getField().equals("isMainDesc")){
                            if(item.getValue().equals("S") || item.getValue().equals("J")){
                                item.setValue("Y");
                            }
                        }
                    }
                }
            }else{
                infoPage.setFilters(null);
            }

        }

        return this.workEffortNoteMapper.getTotale(infoPage.getLimit(), infoPage.getOffset(), infoPage.getFilter(), infoPage.getFilterGenericLabel(), infoPage.getSortOrder(), infoPage.getSortField(), infoPage.getMatchModeSearch(),infoPage.getSecondaryLang());
    }

    @Transactional
    public List<WorkEffortNoteExNoteData> getWorkEffortNoteExNoteDataList(InfoPage infoPage) {
        LOG.info("getWorkEffortNoteExNoteDataList");


        if(infoPage.getFilter() != null){
            boolean control = false;
            for(Filter item: infoPage.getFilter()){
                if(item.getValue() != null){
                    control = true;
                }
            }

            if(control){
                for(Filter item: infoPage.getFilter()){
                    if(item.getValue() != null){
                        if(item.getField().equals("noteDateTime")){
                            Instant instant = Instant.parse(item.getValue());
                            item.setDateValue(instant);
                        }
                        if(item.getField().equals("sequenceId") ){
                            item.setBigDecimalValue(new BigDecimal(item.getValue()));
                        }
                        if(item.getField().equals("isHtmlDesc") || item.getField().equals("internalNoteDesc") || item.getField().equals("isMainDesc")){
                            if(item.getValue().equals("S") || item.getValue().equals("J")){
                                item.setValue("Y");
                            }
                        }
                    }
                }
            }else{
                infoPage.setFilters(null);
            }

        }

        List<WorkEffortNoteExNoteData> workEffortNoteExNoteData = this.workEffortNoteMapper.getWorkEffortNoteExNoteDataList(infoPage.getLimit(), infoPage.getOffset(), infoPage.getFilter(), infoPage.getFilterGenericLabel(), infoPage.getSortOrder(), infoPage.getSortField(), infoPage.getMatchModeSearch(),infoPage.getSecondaryLang());
        LOG.info("size = {}", workEffortNoteExNoteData.size());
        return workEffortNoteExNoteData;
    }

    @Transactional
    public WorkEffortNoteExNoteData getWorkEffortNoteExNoteData(String workEffortId, String noteId, String noteName) {
        LOG.info("getWorkEffortNoteExNoteData");

        WorkEffortNoteExNoteData workEffortNoteExNoteData = this.workEffortNoteMapper.getWorkEffortNoteExNoteData(workEffortId, noteId, noteName);
        LOG.info("WorkEffortNoteExNoteData = {}", (workEffortNoteExNoteData != null));
        return workEffortNoteExNoteData;
    }

    @Transactional
    public WorkEffortNote getWorkEffortNote(String workEffortId, String noteId) {
        LOG.info("find workEffortNote by id");

        WorkEffortNote workEffortNote = this.workEffortNoteMapper.selectByPrimaryKey(workEffortId, noteId);
        LOG.info("WorkEffortNote = {}", (workEffortNote != null));
        return workEffortNote;
    }

    @Transactional
    public List<WorkEffortNote> getWorkEffortNoteByWorkEffortId(String workEffortId) {
        LOG.info("find workEffortNote by workEffortId");
        List<WorkEffortNote> workEffortNoteList = this.workEffortNoteMapper.getWorkEffortNoteByWorkEffortId(workEffortId);
        LOG.info("WorkEffortNoteList = {}", (workEffortNoteList.size()));
        return workEffortNoteList;
    }

    @Transactional
    public boolean create(WorkEffortNote workEffortNote, String userLoginId) {
        LOG.info("create workEffortNote");
        setCreatedTimestamp(workEffortNote);
        workEffortNote.setCreatedByUserLogin(userLoginId);
        int result = this.workEffortNoteMapper.insert(workEffortNote);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean update(WorkEffortNote workEffortNote, String userLoginId) {
        LOG.info("update workEffortNote");
        setUpdateTimestamp(workEffortNote);
        workEffortNote.setLastModifiedByUserLogin(userLoginId);
        int result = this.workEffortNoteMapper.updateByPrimaryKey(workEffortNote);
        LOG.info("result = {}", result);
        return result > 0;
    }

    @Transactional
    public boolean delete(String workEffortId, String noteId) {
        LOG.info("delete workEffortNote");
        int result = this.workEffortNoteMapper.deleteByPrimaryKey(workEffortId, noteId);
        LOG.info("result = {}", result);
        return result > 0;
    }
}
