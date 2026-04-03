package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalStatusRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.*;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile.WorkEffortStatusDtoMapper;
import it.mapsgroup.gzoom.infrastructure.goalfile.util.StatusItemDtoGoalStatusMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GoalStatusMyBatisRepositoryGateway implements GoalStatusRepositoryGateway {
    private final WorkEffortStatusDtoMapper workEffortStatusDtoMapper;
    private final StatusItemDtoGoalStatusMapper statusItemDtoGoalStatusMapper;

    public GoalStatusMyBatisRepositoryGateway(WorkEffortStatusDtoMapper workEffortStatusDtoMapper,
                                              StatusItemDtoGoalStatusMapper statusItemDtoGoalStatusMapper) {
        this.workEffortStatusDtoMapper = workEffortStatusDtoMapper;
        this.statusItemDtoGoalStatusMapper = statusItemDtoGoalStatusMapper;
    }

    @Override
    public GoalStatus getGoalStatus(String id) {
        return statusItemDtoGoalStatusMapper.statusItemDtoToGoalStatus(this.workEffortStatusDtoMapper.selectStatusById(id));
    }

    @Override
    public List<GoalStatusAvailable> getAvailableGoalStatus(String goalFileId,
                                                            String goalFileStatusId) {
        return this.workEffortStatusDtoMapper.selectAvailableGoalStatus(goalFileId, goalFileStatusId).stream()
                .map(statusItemDtoGoalStatusMapper::statusItemDtoToGoalStatusAvailable).toList();
    }
}
