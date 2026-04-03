package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalStatus;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalStatusAvailable;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalStatusChangeDirection;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalStatusType;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.StatusItemChangeDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.StatusItemDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.StatusTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        imports = GoalStatusChangeDirection.class)
public interface StatusItemDtoGoalStatusMapper {
    @Mapping(target = "id", source = "statusId")
    @Mapping(target = "goalFileStatusType", source = "statusType")
    @Mapping(target = "orderStatus", source = "sequenceId")
    GoalStatus statusItemDtoToGoalStatus(StatusItemDto statusItemDto);

    @Mapping(target = "goalStatus", source = "statusItemTo")
    @Mapping(target = "direction", expression = "java(GoalStatusChangeDirection.getGoalStatusChangeDirection(statusItemDto.getDirectionCode()))")
    GoalStatusAvailable statusItemDtoToGoalStatusAvailable(StatusItemChangeDto statusItemDto);

    @Mapping(target = "id", source = "statusTypeId")
    @Mapping(target = "phaseEnumId", source = "phaseStEnumId")
    @Mapping(target = "phaseEnumCode", source = "phaseStEnumCode")
    @Mapping(target = "parentType.id", source = "parentTypeId")
    GoalStatusType statusTypeDtoToGoalStatusType(StatusTypeDto statusTypeDto);
}
