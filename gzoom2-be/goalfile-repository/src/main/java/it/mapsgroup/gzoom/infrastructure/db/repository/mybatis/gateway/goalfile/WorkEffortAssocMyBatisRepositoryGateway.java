package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalAssocRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalAssoc;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile.WorkEffortAssocDtoMapper;
import it.mapsgroup.gzoom.infrastructure.goalfile.util.WorkEffortAssocDtoGoalAssocMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class WorkEffortAssocMyBatisRepositoryGateway implements GoalAssocRepositoryGateway {
    private final WorkEffortAssocDtoMapper workEffortAssocDtoMapper; // va sul db
    private final WorkEffortAssocDtoGoalAssocMapper workEffortAssocDtoGoalAssocMapper; // mappa dto

    public WorkEffortAssocMyBatisRepositoryGateway(WorkEffortAssocDtoMapper workEffortAssocDtoMapper,
                                                   WorkEffortAssocDtoGoalAssocMapper workEffortAssocDtoGoalAssocMapper) {
        this.workEffortAssocDtoMapper = workEffortAssocDtoMapper;
        this.workEffortAssocDtoGoalAssocMapper = workEffortAssocDtoGoalAssocMapper;
    }

    @Override
    public List<GoalAssoc> getGoalFileTreeDetails(String workEffortId, String userLoginId) {
        return this.workEffortAssocDtoMapper.selectWorkEffortRootTreeWithDetails(workEffortId, userLoginId)
                .stream().map(workEffortAssocDtoGoalAssocMapper::workEffortAssocToGoalAssoc).toList();
    }
}
