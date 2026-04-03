package it.mapsgroup.gzoom.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalStatusHistoryRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.Goal;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalStatus;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GoalStatusHistoryUseCase {
    private final GoalStatusHistoryRepositoryGateway goalStatusHistoryRepositoryGateway;

    public GoalStatusHistoryUseCase(GoalStatusHistoryRepositoryGateway goalStatusHistoryRepositoryGateway) {
        this.goalStatusHistoryRepositoryGateway = goalStatusHistoryRepositoryGateway;
    }

    public void createGoalStatusHistory(Goal goal,
                                        GoalStatus goalStatus,
                                        String userLoginId) {
        this.goalStatusHistoryRepositoryGateway.createGoalFileStatusHistory(goal, goalStatus, userLoginId);
    }
}
