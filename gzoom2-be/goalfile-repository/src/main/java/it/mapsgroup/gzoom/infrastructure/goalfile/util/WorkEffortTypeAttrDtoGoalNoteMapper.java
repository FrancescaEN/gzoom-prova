package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalNote;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.NoteDataDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortNoteDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortTypeAttrDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkEffortTypeAttrDtoGoalNoteMapper {
    @Mapping(target = "goalTypeId", source = "workEffortTypeId")
    @Mapping(target = "name", source = "attrName")
    @Mapping(target = "nameLang", source = "attrNameLang")
    @Mapping(target = "id", source = "noteId")
    GoalNote workEffortTypeAttrToGoalNote(WorkEffortTypeAttrDto goalNote);

    @Mapping(target = "id", source = "noteId")
    @Mapping(target = "name", source = "noteName")
    @Mapping(target = "nameLang", source = "noteNameLang")
    @Mapping(target = "goalId", source = "workEffortId")
    @Mapping(target = "info", source = "noteInfo")
    @Mapping(target = "infoLang", source = "noteInfoLang")
    @Mapping(target = "dateTime", source = "noteDateTime")
    @Mapping(target = "party", source = "noteParty")
    GoalNote workEffortNoteToGoalNote(WorkEffortNoteDto goalNote);

    @Mapping(source = "id", target = "noteId")
    @Mapping(source = "name", target = "noteName")
    @Mapping(source = "nameLang", target = "noteNameLang")
    @Mapping(source = "dateTime", target = "noteDateTime")
    @Mapping(source = "party", target = "noteParty")
    @Mapping(source = "info", target = "noteInfo")
    @Mapping(source = "infoLang", target = "noteInfoLang")
    NoteDataDto goalNoteToNoteDataDto(GoalNote goalNote);

    @Mapping(target = "noteId", source = "goalNote.id")
    @Mapping(target = "workEffortId", source = "goalId")
    @Mapping(target = "noteName", source = "goalNote.name")
    @Mapping(target = "noteNameLang", source = "goalNote.nameLang")
    @Mapping(target = "noteInfo", source = "goalNote.info")
    @Mapping(target = "noteInfoLang", source = "goalNote.infoLang")
    @Mapping(target = "noteDateTime", source = "goalNote.dateTime")
    @Mapping(target = "noteParty", source = "goalNote.party")
    WorkEffortNoteDto goalNoteToWorkEffortNoteDto(GoalNote goalNote, String goalId);
}
