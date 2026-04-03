package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalFile;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile.GoalFileMapper;
import it.mapsgroup.gzoom.infrastructure.goalfile.util.WorkEffortDtoGoalMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
public class WorkEffortMyBatisRepositoryGateway implements GoalRepositoryGateway {
    private final GoalFileMapper workEffortMapper;
    private final WorkEffortDtoGoalMapper workEffortDtoGoalMapper;

    public WorkEffortMyBatisRepositoryGateway(GoalFileMapper workEffortMapper,
                                              WorkEffortDtoGoalMapper workEffortDtoGoalMapper) {
        this.workEffortMapper = workEffortMapper;
        this.workEffortDtoGoalMapper = workEffortDtoGoalMapper;
    }

    @Override
    public List<GoalFile> getGoalFileRoots(String userLoginId) {
        return this.workEffortMapper.selectWorkEffortRoots(userLoginId)
                .stream().map(workEffortDtoGoalMapper::workEffortDtoToGoalFile).toList();
    }

    @Override
    public GoalFile getGoalFileById(String id) {
        return workEffortDtoGoalMapper.workEffortDtoToGoalFile(this.workEffortMapper.searchWorkEffortById(id, Instant.now()));
    }

    @Override
    public void updateGoalFile(GoalFile goalFile, String userLoginId) {
        log.info("update goalFile");
        var now = OffsetDateTime.now();
        workEffortMapper.updateWorkEffort(this.workEffortDtoGoalMapper.goalFileToWorkEffortDto(goalFile)
                .setLastModifiedDate(now)
                .setLastModifiedByUserLogin(userLoginId)
                .setLastUpdatedStamp(now)
                .setLastUpdatedTxStamp(now));
    }

    @Override
    public List<GoalFile> getChildren(String goalFileTypeId) {
        return workEffortMapper.searchReferenceWorkEffortChildren(goalFileTypeId)
                .stream().map(workEffortDtoGoalMapper::workEffortDtoToGoalFile).toList();
    }
}
