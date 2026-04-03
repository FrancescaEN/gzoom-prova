package it.mapsgroup.gzoom.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalAssocRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalAssoc;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GoalAssocUseCase {
    private final GoalAssocRepositoryGateway goalAssocRepositoryGateway;

    public GoalAssocUseCase(GoalAssocRepositoryGateway goalAssocRepositoryGateway) {
        this.goalAssocRepositoryGateway = goalAssocRepositoryGateway;
    }

    public List<GoalAssoc> getGoalFileTreeDetails(String goalId, String userLoginId) {
        return this.goalAssocRepositoryGateway.getGoalFileTreeDetails(goalId, userLoginId);
    }
}
