package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.Goal;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalFile;
import it.mapsgroup.gzoom.entity.goalfile.model.GoalType;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortTypeDto;
import it.mapsgroup.gzoom.infrastructure.orgchart.util.PartyDtoOrganizationUnitMapper;
import it.mapsgroup.gzoom.infrastructure.orgchart.util.RoleTypeDtoOrganizationUnitTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {PartyDtoOrganizationUnitMapper.class,
                RoleTypeDtoOrganizationUnitTypeMapper.class,
                StatusItemDtoGoalStatusMapper.class,
                WorkEffortPurposeTypeDtoGoalPurposeTypeMapper.class,
                WorkEffortRevisionDtoGoalRevisionMapper.class,
                WorkEffortMeasureDtoGoalIndicatorMapper.class,
                WorkEffortNoteDtoGoalNoteMapper.class,
                WorkEffortTypePeriodDtoGoalPeriodMapper.class}
)
public interface WorkEffortDtoGoalMapper {
//    List<GoalFile> workEffortDtoListToGoalFileList(List<WorkEffortDto> goalFileList);

    @Mapping(target = "id", source = "workEffortId")
    @Mapping(target = "code", source = "sourceReferenceId")
    @Mapping(target = "etch", source = "etch")
    @Mapping(target = "name", source = "workEffortName")
    @Mapping(target = "nameLang", source = "workEffortNameLang")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "descriptionLang", source = "descriptionLang")
    @Mapping(target = "fromDate", source = "estimatedStartDate")
    @Mapping(target = "thruDate", source = "estimatedCompletionDate")
    @Mapping(target = "scheduledStartDate", source = "scheduledStartDate")
    @Mapping(target = "scheduledCompletionDate", source = "scheduledCompletionDate")
    @Mapping(target = "actualStartDate", source = "actualStartDate")
    @Mapping(target = "actualCompletionDate", source = "actualCompletionDate")
    @Mapping(target = "referenceDate", source = "refDate")
    @Mapping(target = "organizationUnitType", source = "orgUnitRoleType")
    @Mapping(target = "organizationUnit", source = "orgUnitParty")
    @Mapping(target = "supervisorOrganizationUnit", source = "supUnitParty")
    @Mapping(target = "topOrganizationUnit", source = "topUnitParty")
    @Mapping(target = "goalStatus", source = "statusItem")
    @Mapping(target = "lastStatusUpdate", source = "lastStatusUpdate")
    @Mapping(target = "goalRevision", source = "workEffortRevision")
    @Mapping(target = "goalFileParent", source = "workEffortParent")
    @Mapping(target = "goalType", source = "workEffortType")
    @Mapping(target = "posted", source = "posted")
    @Mapping(target = "lastModifiedByUserLogin", source = "lastModifiedByUserLogin")
    @Mapping(target = "lastUpdatedStamp", source = "lastUpdatedStamp")
    @Mapping(target = "goalPurposeType", source = "workEffortPurposeType")
    @Mapping(target = "indicators", source = "workEffortMeasures")
    @Mapping(target = "notes", source = "workEffortNotes")
    @Mapping(target = "goalPeriod", source = "workEffortTypePeriod")
    GoalFile workEffortDtoToGoalFile(WorkEffortDto workEffortDto);

    @Mapping(target = "id", source = "workEffortTypeId")
    GoalType workEffortDtoToGoalType(WorkEffortTypeDto workEffortTypeDto);

    @Mapping(source = "id", target = "workEffortId")
    @Mapping(source = "code", target = "sourceReferenceId")
    @Mapping(source = "etch", target = "etch")
    @Mapping(source = "name", target = "workEffortName")
    @Mapping(source = "nameLang", target = "workEffortNameLang")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "descriptionLang", target = "descriptionLang")
    @Mapping(source = "fromDate", target = "estimatedStartDate")
    @Mapping(source = "thruDate", target = "estimatedCompletionDate")
    @Mapping(source = "goalType.id", target = "workEffortType.workEffortTypeId")
    @Mapping(source = "organizationUnit", target = "orgUnitParty")
    @Mapping(source = "goalStatus.id", target = "statusItem.statusId")
    @Mapping(source = "goalPeriod.id", target = "workEffortTypePeriod.workEffortTypePeriodId")
    @Mapping(source = "goalPurposeType.id", target = "workEffortPurposeType.workEffortPurposeTypeId")
    @Mapping(source = "goalRevision.id", target = "workEffortRevision.workEffortRevisionId")
    @Mapping(source = "goalFileParent.id", target = "workEffortParent.workEffortId")
    WorkEffortDto goalFileToWorkEffortDto(Goal goal);

//    default String map(DataResourceDto icon) {
//        return icon != null ? icon.getObjectInfo() : null;
//    }
}
