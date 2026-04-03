package it.mapsgroup.gzoom.entity.goalfile.gateway;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalAssoc;

import java.util.List;

public interface GoalAssocRepositoryGateway {
    List<GoalAssoc> getGoalFileTreeDetails(String goalId, String userLoginId);
}
