package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalNote;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortNoteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkEffortNoteDtoGoalNoteMapper {
    List<GoalNote> workEffortNoteListToGoalNoteList(List<WorkEffortNoteDto> goalNoteList);

    @Mapping(target = "id", source = "noteId")
    @Mapping(target = "name", source = "noteName")
    @Mapping(target = "nameLang", source = "noteNameLang")
    @Mapping(target = "goalId", source = "workEffortId")
    @Mapping(target = "info", source = "noteInfo")
    @Mapping(target = "infoLang", source = "noteInfoLang")
    @Mapping(target = "dateTime", source = "noteDateTime")
    @Mapping(target = "party", source = "noteParty")
    GoalNote workEffortNoteToGoalNote(WorkEffortNoteDto goalNote);
}
