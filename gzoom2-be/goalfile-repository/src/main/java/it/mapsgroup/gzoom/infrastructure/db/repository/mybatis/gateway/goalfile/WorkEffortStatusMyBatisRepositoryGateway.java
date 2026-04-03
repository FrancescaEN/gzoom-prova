package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalStatusHistoryRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.Goal;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalStatus;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile.WorkEffortStatusDtoMapper;
import it.mapsgroup.gzoom.infrastructure.goalfile.util.WorkEffortStatusDtoGoalStatusMapper;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class WorkEffortStatusMyBatisRepositoryGateway implements GoalStatusHistoryRepositoryGateway {
    private final WorkEffortStatusDtoMapper workEffortStatusDtoMapper;
    private final WorkEffortStatusDtoGoalStatusMapper workEffortStatusDtoGoalStatusMapper;

    public WorkEffortStatusMyBatisRepositoryGateway(WorkEffortStatusDtoMapper workEffortStatusDtoMapper,
                                                    WorkEffortStatusDtoGoalStatusMapper workEffortStatusDtoGoalStatusMapper) {
        this.workEffortStatusDtoMapper = workEffortStatusDtoMapper;
        this.workEffortStatusDtoGoalStatusMapper = workEffortStatusDtoGoalStatusMapper;
    }

    @Override
    public void createGoalFileStatusHistory(Goal goal,
                                            GoalStatus goalStatus,
                                            String userLoginId) {
        workEffortStatusDtoMapper.createWorkEffortStatus(
                workEffortStatusDtoGoalStatusMapper.goalStatusToWorkEffortStatusDto(goalStatus, goal.getId()),
                userLoginId, Instant.now());
    }
}
