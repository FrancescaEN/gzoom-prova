package it.mapsgroup.gzoom.entity.goalfile.gateway;

import it.mapsgroup.gzoom.entity.goalfile.model.Goal;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalStatus;


public interface GoalStatusHistoryRepositoryGateway {
    void createGoalFileStatusHistory(Goal goal,
                                     GoalStatus goalStatus,
                                     String userLoginId);
}
