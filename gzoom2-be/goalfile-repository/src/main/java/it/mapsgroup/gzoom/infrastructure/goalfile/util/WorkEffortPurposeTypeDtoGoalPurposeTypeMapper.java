package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalPurposeType;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortPurposeTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkEffortPurposeTypeDtoGoalPurposeTypeMapper {
    List<GoalPurposeType> workEffortPurposeTypeListToGoalFilePurposeTypeList(List<WorkEffortPurposeTypeDto> goalFilePurposeTypeList);

    @Mapping(target = "id", source = "workEffortPurposeTypeId")
    @Mapping(target = "code", source = "workEffortPurposeTypeCode")
    GoalPurposeType workEffortPurposeTypeToGoalFilePurposeType(WorkEffortPurposeTypeDto goalFilePurposeType);
}
