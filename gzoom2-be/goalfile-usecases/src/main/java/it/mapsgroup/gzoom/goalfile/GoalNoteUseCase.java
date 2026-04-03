package it.mapsgroup.gzoom.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalNoteRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalNote;
import it.mapsgroup.gzoom.goalfile.exception.GoalNoteCreationException;
import it.mapsgroup.gzoom.goalfile.exception.GoalNoteNotFoundException;
import it.mapsgroup.gzoom.sequencegenerator.usecase.SequenceGeneratorUseCase;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GoalNoteUseCase {
    private final GoalNoteRepositoryGateway goalNoteRepositoryGateway;
    private final SequenceGeneratorUseCase sequenceGeneratorUseCase;

    public GoalNoteUseCase(GoalNoteRepositoryGateway goalNoteRepositoryGateway,
                           SequenceGeneratorUseCase sequenceGeneratorUseCase) {
        this.goalNoteRepositoryGateway = goalNoteRepositoryGateway;
        this.sequenceGeneratorUseCase = sequenceGeneratorUseCase;
    }

    public List<GoalNote> getGoalNoteListByWorkEffortId(String workEffortId) {
        List<GoalNote> result = this.goalNoteRepositoryGateway.getGoalNoteListByWorkEffortId(workEffortId);
        log.info("getGoalFileNotesByWorkEffortId result={}", result);
        return result;
    }

    public void createGoalNote(GoalNote goalNote, String userLoginId) throws GoalNoteCreationException {
        goalNote.setId(this.sequenceGeneratorUseCase.getNextSeqId("NoteData"));
        boolean result = this.goalNoteRepositoryGateway.createGoalFileNote(goalNote, userLoginId);
        log.info("createGoalNote result={}", result);
        if (!result) {
            throw new GoalNoteCreationException("Error creating note");
        }
    }

    public void updateGoalNote(GoalNote goalNote, String userLoginId) throws GoalNoteNotFoundException {
       boolean result = this.goalNoteRepositoryGateway.updateGoalFileNote(goalNote, userLoginId);
        log.info("updateGoalNote result={}", result);
        if (!result) {
            throw new GoalNoteNotFoundException("Note not found");
        }
    }

    public void deleteGoalNote(String noteId) throws GoalNoteNotFoundException {
        boolean result = this.goalNoteRepositoryGateway.deleteGoalFileNote(noteId);
        log.info("deleteGoalNote result={}", result);
        if (!result) {
            throw new GoalNoteNotFoundException("Note not found");
        }
    }
}
