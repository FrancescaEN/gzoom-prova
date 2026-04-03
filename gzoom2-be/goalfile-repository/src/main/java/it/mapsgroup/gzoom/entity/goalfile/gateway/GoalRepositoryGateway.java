package it.mapsgroup.gzoom.entity.goalfile.gateway;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalFile;

import java.util.List;

public interface GoalRepositoryGateway {
    List<GoalFile> getGoalFileRoots(String userLoginId);

    GoalFile getGoalFileById(String id);

    void updateGoalFile(GoalFile goalFile, String userLoginId);

    List<GoalFile> getChildren(String goalFileId);
}
