package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalContent;
import it.mapsgroup.gzoom.infrastructure.content.dto.ContentDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortContentDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ContentDtoGoalContentMapper {

    @Mapping(target = "contentId", source = "contentId")
    @Mapping(target = "statusId", source = "contentStatusId")
    ContentDto goalContentToContent(GoalContent goalContent);

    @InheritInverseConfiguration
    GoalContent contentToGoalContent(ContentDto contentDto);
}
