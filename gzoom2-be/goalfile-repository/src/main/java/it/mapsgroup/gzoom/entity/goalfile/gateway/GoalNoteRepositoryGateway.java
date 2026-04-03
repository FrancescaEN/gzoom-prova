package it.mapsgroup.gzoom.entity.goalfile.gateway;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalNote;
import jakarta.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GoalNoteRepositoryGateway {
    List<GoalNote> getGoalNoteListByWorkEffortId(@NotNull String workEffortId);

    boolean createGoalFileNote(GoalNote goalNote, String userLoginId);

    boolean updateGoalFileNote(GoalNote goalNote, String userLoginId);

    @Transactional
    boolean deleteGoalFileNote(String noteId);
}
