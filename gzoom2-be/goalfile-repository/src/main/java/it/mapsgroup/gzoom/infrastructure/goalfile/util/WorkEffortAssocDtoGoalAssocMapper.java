package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalAssoc;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortAssocDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortMeasureDto;
import it.mapsgroup.gzoom.infrastructure.uom.util.UomDtoUomMapper;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.Instant;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        imports = Instant.class, uses = {WorkEffortDtoGoalMapper.class, WorkEffortMeasureDtoGoalIndicatorMapper.class, UomDtoUomMapper.class})
public interface WorkEffortAssocDtoGoalAssocMapper {

    @Mapping(target = "workEffortFrom", source = "fromGoal")
    @Mapping(target = "workEffortTo", source = "toGoal")
    @Mapping(target = "workEffortAssocTypeId", source = "assocTypeId")
    @Mapping(target = "comments", source = "comments")
    @Mapping(target = "commentsLang", source = "commentsLang")
    WorkEffortAssocDto goalAssocToWorkEffortAssocDto(GoalAssoc goalAssoc);

    @InheritInverseConfiguration
    GoalAssoc workEffortAssocToGoalAssoc(WorkEffortAssocDto workEffortAssocDto);

    List<GoalAssoc> workEffortAssocListToGoalAssocList(List<WorkEffortAssocDto> workEffortAssocDto);
}
