package it.mapsgroup.gzoom.entity.goalfile.gateway;

import it.mapsgroup.gzoom.entity.goalfile.model.*;

import java.util.List;

public interface GoalStatusRepositoryGateway {
    GoalStatus getGoalStatus(String id);

    List<GoalStatusAvailable> getAvailableGoalStatus(String goalFileId,
                                                     String goalFileStatusId);
}
