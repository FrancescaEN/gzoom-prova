package it.mapsgroup.gzoom.goalfile;

import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalStatusRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.*;
import it.mapsgroup.gzoom.entity.queryconfig.model.ResultMessage;
import it.mapsgroup.gzoom.goalfile.exception.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;

import static it.mapsgroup.gzoom.security.Principals.username;


@Slf4j
public class GoalStatusUseCase {
    private final GoalRepositoryGateway goalFileRepositoryGateway;
    private final GoalStatusRepositoryGateway goalStatusRepositoryGateway;
    private final GoalNoteUseCase goalNoteUseCase;
    private final GoalStatusHistoryUseCase goalStatusHistoryUseCase;

    public GoalStatusUseCase(GoalRepositoryGateway goalFileRepositoryGateway,
                             GoalStatusRepositoryGateway goalStatusRepositoryGateway,
                             GoalNoteUseCase goalNoteUseCase,
                             GoalStatusHistoryUseCase goalStatusHistoryUseCase) {
        this.goalFileRepositoryGateway = goalFileRepositoryGateway;
        this.goalStatusRepositoryGateway = goalStatusRepositoryGateway;
        this.goalNoteUseCase = goalNoteUseCase;
        this.goalStatusHistoryUseCase = goalStatusHistoryUseCase;
    }

    public GoalStatus getGoalStatus(String id) throws GoalStatusNotFoundException {
        GoalStatus status = this.goalStatusRepositoryGateway.getGoalStatus(id);
        log.debug("getGoalStatus status={}", status);
        if (status == null) {
            throw new GoalStatusNotFoundException("Status not found [id=" + id + "]");
        }
        return status;
    }

    public List<GoalStatusAvailable> getGoalFileAvailableStatus(String goalFileId,
                                                                String goalFileStatusId) {
        var result = this.goalStatusRepositoryGateway.getAvailableGoalStatus(goalFileId, goalFileStatusId);
        log.info("getGoalFileAvailableStatus result={}", result.size());
        return result;
    }

    @Transactional
    public Results<Boolean> updateGoalFileStatus(String goalFileId,
                                                 @Valid GoalStatus goalStatusSrc,
                                                 String noteId) throws GoalFileNotFoundException {
        List<ResultMessage> resultMessageList = new ArrayList<>();
        Results<Boolean> r = new Results<>();
        var userLoginId = username();

        var goalFileTgt = this.goalFileRepositoryGateway.getGoalFileById(goalFileId);
        log.info("update status goalFile={}", goalFileTgt);
        if (goalFileTgt == null) {
            throw new GoalFileNotFoundException("No goalFile for id = " + goalFileId);
        }

        if (goalStatusSrc != null && goalStatusSrc.getId() != null
                && !goalStatusSrc.getId().equals(goalFileTgt.getGoalStatus().getId())) {
            try {
                var newStatus = getGoalStatus(goalStatusSrc.getId());
                if (noteId != null && !noteId.isEmpty() && !goalStatusSrc.getReason().isEmpty()) {
                    // saving comment as a note, not as the status change reason (GN-8349)
                    goalNoteUseCase.updateGoalNote(new GoalNote().setId(noteId).setInfo(goalStatusSrc.getReason()),
                            userLoginId);
                    goalStatusSrc.setReason(null);
                }
                changeStatus(goalFileId, newStatus, goalStatusSrc, goalFileTgt, userLoginId);
            } catch (GoalStatusNotFoundException | GoalNoteNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        r.setData(true);
        r.setMessages(resultMessageList);
        return r;
    }

    private void changeStatus(String goalFileId,
                              GoalStatus status,
                              GoalStatus goalStatusSrc,
                              GoalFile goalFileTgt,
                              String userLoginId) {
        if (goalStatusSrc.getReason() != null) {
            status.setReason(goalStatusSrc.getReason());
        }
        var now = OffsetDateTime.now();

        goalFileTgt.setGoalStatus(status);
        goalFileTgt.setLastStatusUpdate(now);
        goalFileRepositoryGateway.updateGoalFile(goalFileTgt, userLoginId);
        goalStatusHistoryUseCase.createGoalStatusHistory(goalFileTgt, status, userLoginId);

        goalFileRepositoryGateway.getChildren(goalFileId).forEach(child -> {
            child.setGoalStatus(status);
            child.setLastStatusUpdate(now);
            goalFileRepositoryGateway.updateGoalFile(child, userLoginId);
            goalStatusHistoryUseCase.createGoalStatusHistory(child, status, userLoginId);
        });
    }
}
