package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalStatus;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortStatusDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.OffsetDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        imports = OffsetDateTime.class)
public interface WorkEffortStatusDtoGoalStatusMapper {
    @Mapping(target = "statusId", source = "goalStatus.id")
    @Mapping(target = "statusDateTime", source = "goalStatus.statusDateTime", defaultExpression = "java(OffsetDateTime.now())")
    WorkEffortStatusDto goalStatusToWorkEffortStatusDto(GoalStatus goalStatus, String workEffortId);
}
