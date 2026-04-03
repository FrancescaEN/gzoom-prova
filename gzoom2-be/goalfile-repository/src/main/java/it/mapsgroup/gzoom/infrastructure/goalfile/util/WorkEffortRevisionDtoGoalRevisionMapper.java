package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalRevision;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortRevisionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkEffortRevisionDtoGoalRevisionMapper {

    List<GoalRevision> workEffortRevisionDtoListToGoalRevisionList(List<WorkEffortRevisionDto> workEffortRevisionDtoList);

    @Mapping(target = "id", source = "workEffortRevisionId")
    GoalRevision workEffortDtoToGoalRevision(WorkEffortRevisionDto workEffortRevisionDto);
}
