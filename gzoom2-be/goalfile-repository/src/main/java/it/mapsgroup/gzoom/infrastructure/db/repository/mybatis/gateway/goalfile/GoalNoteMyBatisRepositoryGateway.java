package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalNoteRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalNote;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile.GoalNoteMapper;
import it.mapsgroup.gzoom.infrastructure.goalfile.util.WorkEffortTypeAttrDtoGoalNoteMapper;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
public class GoalNoteMyBatisRepositoryGateway implements GoalNoteRepositoryGateway {
    private final GoalNoteMapper goalNoteMapper; // va sul db
    private final WorkEffortTypeAttrDtoGoalNoteMapper workEffortTypeAttrDtoGoalNoteMapper; // mappa dto

    public GoalNoteMyBatisRepositoryGateway(GoalNoteMapper goalNoteMapper
            , WorkEffortTypeAttrDtoGoalNoteMapper workEffortTypeAttrDtoGoalNoteMapper) {
        this.goalNoteMapper = goalNoteMapper;
        this.workEffortTypeAttrDtoGoalNoteMapper = workEffortTypeAttrDtoGoalNoteMapper;
    }

    @Override
    public List<GoalNote> getGoalNoteListByWorkEffortId(@NotNull String workEffortId) {
        return goalNoteMapper.selectWorkEffortNotesFromWorkEffortId(workEffortId, null, null, false)
                .stream().map(workEffortTypeAttrDtoGoalNoteMapper::workEffortNoteToGoalNote).toList();
    }

    @Override
    @Transactional
    public boolean createGoalFileNote(GoalNote goalNote, String userLoginId) {
        int insertNoteData = this.goalNoteMapper.insertNoteData(
                this.workEffortTypeAttrDtoGoalNoteMapper.goalNoteToNoteDataDto(goalNote),
                userLoginId,
                Instant.now());
        int insertWorkEffortNote = this.goalNoteMapper.insertWorkEffortNote(
                this.workEffortTypeAttrDtoGoalNoteMapper.goalNoteToWorkEffortNoteDto(goalNote, goalNote.getGoalId()),
                userLoginId,
                Instant.now());
        return insertNoteData > 0 && insertWorkEffortNote > 0;
    }


    @Override
    @Transactional
    public boolean updateGoalFileNote(GoalNote goalNote, String userLoginId) {
        int updateWorkEffortNote = this.goalNoteMapper.updateWorkEffortNote(
                this.workEffortTypeAttrDtoGoalNoteMapper.goalNoteToWorkEffortNoteDto(goalNote, goalNote.getId()),
                userLoginId,
                Instant.now());
        int updateNoteData = this.goalNoteMapper.updateNoteData(
                this.workEffortTypeAttrDtoGoalNoteMapper.goalNoteToNoteDataDto(goalNote),
                userLoginId,
                Instant.now());
        return updateWorkEffortNote > 0 && updateNoteData > 0;
    }

    @Override
    @Transactional
    public boolean deleteGoalFileNote(String noteId) {
        int deleteWorkEffortNote = this.goalNoteMapper.deleteWorkEffortNote(noteId);

        int deleteNoteData = this.goalNoteMapper.deleteNoteData(noteId);

        return deleteWorkEffortNote > 0 && deleteNoteData > 0;
    }
}
