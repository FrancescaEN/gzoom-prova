package it.mapsgroup.gzoom.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.*;
import it.mapsgroup.gzoom.entity.goalfile.util.GoalMapper;
import it.mapsgroup.gzoom.goalfile.exception.*;

import it.mapsgroup.gzoom.util.GenericTreeBuilder;
import lombok.extern.slf4j.Slf4j;


import java.util.List;

@Slf4j
public class GoalFileUseCase {
    private final GoalAssocUseCase goalAssocUseCase;
    private final GoalMapper goalMapper;
    private final GoalRepositoryGateway goalRepositoryGateway;

    public GoalFileUseCase(GoalAssocUseCase goalAssocUseCase,
                           GoalMapper goalMapper,
                           GoalRepositoryGateway goalRepositoryGateway) {
        this.goalAssocUseCase = goalAssocUseCase;
        this.goalMapper = goalMapper;
        this.goalRepositoryGateway = goalRepositoryGateway;
    }

    public List<Goal> getGoalFileTreeDetails(String goalId, String userLoginId) throws GoalFileNotFoundException {
        List<GoalAssoc> goalAssocList = this.goalAssocUseCase.getGoalFileTreeDetails(goalId, userLoginId);

        if (goalAssocList.isEmpty()) {
            throw new GoalFileNotFoundException("No goalFile tree for goalId = " + goalId);
        }

        GenericTreeBuilder<GoalAssoc, String, Goal> builder = new GenericTreeBuilder<GoalAssoc, String, Goal>(GoalAssoc::getGoalIdTo, GoalAssoc::getGoalIdFrom, goalMapper::extractToGoalFromGoalAssoc);
        return builder.buildTree(goalAssocList);
    }

    public List<GoalFile> getGoalFileRoots(String userLoginId) {
        return this.goalRepositoryGateway.getGoalFileRoots(userLoginId);
    }
}