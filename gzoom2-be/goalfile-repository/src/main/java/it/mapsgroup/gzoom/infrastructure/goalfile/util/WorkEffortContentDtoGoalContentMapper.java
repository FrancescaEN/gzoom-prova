package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalContent;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortContentDetailDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortContentDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkEffortContentDtoGoalContentMapper {
    @InheritInverseConfiguration
    GoalContent workEffortContentDetailToGoalContent(WorkEffortContentDetailDto workEffortContentDetailDto);

    @Mapping(target = "workEffortId", source = "goalId")
    @Mapping(target = "contentId", source = "contentId")
    @Mapping(target = "workEffortContentTypeId", source = "goalContentTypeId")
    @Mapping(target = "fromDate", source = "fromDate")
    @Mapping(target = "thruDate", source = "thruDate")
    WorkEffortContentDto goalContentToWorkEffortContent(GoalContent goalContent);
}
