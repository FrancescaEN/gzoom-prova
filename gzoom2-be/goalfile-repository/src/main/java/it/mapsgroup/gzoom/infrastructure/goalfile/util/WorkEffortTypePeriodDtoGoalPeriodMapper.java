package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalPeriod;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortTypePeriodDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkEffortTypePeriodDtoGoalPeriodMapper {
    @Mapping(target = "periodName", source = "customTimePeriod.periodName")
    @Mapping(target = "fromDate", source = "customTimePeriod.fromDate")
    @Mapping(target = "thruDate", source = "customTimePeriod.thruDate")
    GoalPeriod workEffortTypePeriodDtoToGoalPeriod(WorkEffortTypePeriodDto workEffortTypePeriodDto);
}
