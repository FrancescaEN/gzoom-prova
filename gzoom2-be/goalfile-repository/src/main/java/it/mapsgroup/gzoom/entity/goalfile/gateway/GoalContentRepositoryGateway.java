package it.mapsgroup.gzoom.entity.goalfile.gateway;

import com.drew.lang.annotations.NotNull;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalContent;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;


public interface GoalContentRepositoryGateway {
    List<GoalContent> getGoalContentListByWorkEffortId(@jakarta.validation.constraints.NotNull String workEffortId,
                                                       Instant fromDate,
                                                       Instant thruDate);

    boolean createGoalContent(GoalContent goalContent, String userLoginId);

    GoalContent getGoalContent(String contentId);

    boolean updateGoalContent(GoalContent goalContent, String userLoginId);

    String getObjectInfoByContentId(@NotNull String contentId);

    String getMimeTypeIdByFileExtensionId(@NotNull String fileExtensionId);

    @Transactional
    boolean deleteGoalContent(String contentId);
}
