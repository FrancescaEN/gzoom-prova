package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalContent;
import it.mapsgroup.gzoom.infrastructure.content.dto.DataResourceDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DataResourceDtoGoalContentMapper {

    @Mapping(target = "dataResourceId", source = "dataResourceId")
    @Mapping(target = "dataResourceName", source = "contentName")
    @Mapping(target = "statusId", source = "dataResourceStatusId")
    DataResourceDto goalContentToDataResource(GoalContent goalContent);

    @InheritInverseConfiguration
    GoalContent dataResourceToGoalContent(DataResourceDto dataResourceDto);
}
