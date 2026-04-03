package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.goalfile;

import com.drew.lang.annotations.NotNull;
import it.mapsgroup.gzoom.entity.goalfile.gateway.GoalContentRepositoryGateway;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalContent;
import it.mapsgroup.gzoom.infrastructure.content.dto.DataResourceDto;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.goalfile.GoalContentMapper;
import it.mapsgroup.gzoom.infrastructure.goalfile.util.ContentDtoGoalContentMapper;
import it.mapsgroup.gzoom.infrastructure.goalfile.util.DataResourceDtoGoalContentMapper;
import it.mapsgroup.gzoom.infrastructure.goalfile.util.WorkEffortContentDtoGoalContentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;


@Slf4j
public class GoalContentMyBatisRepositoryGateway implements GoalContentRepositoryGateway {
    private final GoalContentMapper goalContentMapper; // va sul db
    private final WorkEffortContentDtoGoalContentMapper workEffortContentDtoGoalContentMapper;
    private final ContentDtoGoalContentMapper contentDtoGoalContentMapper;
    private final DataResourceDtoGoalContentMapper dataResourceDtoGoalContentMapper;

    public GoalContentMyBatisRepositoryGateway(GoalContentMapper goalContentMapper,
                                               WorkEffortContentDtoGoalContentMapper workEffortContentDtoGoalContentMapper,
                                               ContentDtoGoalContentMapper contentDtoGoalContentMapper,
                                               DataResourceDtoGoalContentMapper dataResourceDtoGoalContentMapper) {
        this.goalContentMapper = goalContentMapper;
        this.workEffortContentDtoGoalContentMapper = workEffortContentDtoGoalContentMapper;
        this.contentDtoGoalContentMapper = contentDtoGoalContentMapper;
        this.dataResourceDtoGoalContentMapper = dataResourceDtoGoalContentMapper;
    }

    @Override
    public List<GoalContent> getGoalContentListByWorkEffortId(@jakarta.validation.constraints.NotNull String workEffortId,
                                                              Instant fromDate,
                                                              Instant thruDate) {
        return this.goalContentMapper.selectWorkEffortContentDetailListFromWorkEffortId(workEffortId, fromDate, thruDate)
                .stream().map(workEffortContentDtoGoalContentMapper::workEffortContentDetailToGoalContent).toList();
    }

    @Override
    @Transactional
    public boolean createGoalContent(GoalContent goalContent, String userLoginId) {
        int insertDataResource = this.goalContentMapper.insertDataResource(
                this.dataResourceDtoGoalContentMapper.goalContentToDataResource(goalContent),
                userLoginId,
                Instant.now());
        int insertContent = this.goalContentMapper.insertContent(
                this.contentDtoGoalContentMapper.goalContentToContent(goalContent),
                userLoginId,
                Instant.now());
        int insertWorkEffortContent = this.goalContentMapper.insertWorkEffortContent(
                this.workEffortContentDtoGoalContentMapper.goalContentToWorkEffortContent(goalContent),
                userLoginId,
                Instant.now());
        return insertContent > 0 && insertDataResource > 0 && insertWorkEffortContent > 0;
    }

    @Override
    public GoalContent getGoalContent(String contentId) {
        return dataResourceDtoGoalContentMapper.dataResourceToGoalContent(
                goalContentMapper.selectDataResourceFromDataResourceId(contentId)
        );
    }

    @Override
    @Transactional
    public boolean updateGoalContent(GoalContent goalContent, String userLoginId) {
        int updateDataResource = this.goalContentMapper.updateDataResource(
                this.dataResourceDtoGoalContentMapper.goalContentToDataResource(goalContent),
                userLoginId,
                Instant.now());
        int updateContent = this.goalContentMapper.updateContent(
                this.contentDtoGoalContentMapper.goalContentToContent(goalContent),
                userLoginId,
                Instant.now());
        int updateWorkEffortContent = this.goalContentMapper.updateWorkEffortContent(
                this.workEffortContentDtoGoalContentMapper.goalContentToWorkEffortContent(goalContent),
                userLoginId,
                Instant.now());
        return updateContent > 0 && updateDataResource > 0 && updateWorkEffortContent > 0;
    }

    @Override
    public String getObjectInfoByContentId(@NotNull String contentId) {
        DataResourceDto dataResourceDto = this.goalContentMapper.selectDataResourceFromDataResourceId(contentId);
        if (dataResourceDto == null) {
            return null;
        }
        return dataResourceDto.getObjectInfo();
    }

    @Override
    public String getMimeTypeIdByFileExtensionId(@NotNull String fileExtensionId) {
        var fileExtensionDto = this.goalContentMapper.selectMimeTypeIdByFileExtensionId(fileExtensionId);
        return fileExtensionDto == null ? null : fileExtensionDto.getMimeTypeId();
    }

    @Override
    @Transactional
    public boolean deleteGoalContent(@NotNull String contentId) {

        int deleteWorkEffortContent = this.goalContentMapper.deleteWorkEffortContent(contentId);

        int deleteContent = this.goalContentMapper.deleteContent(contentId);

        int deleteDataResource = this.goalContentMapper.deleteDataResource(contentId);


        return deleteWorkEffortContent > 0 && deleteContent > 0 && deleteDataResource > 0;
    }
}
