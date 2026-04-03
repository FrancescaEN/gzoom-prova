
package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.*;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.*;
import it.mapsgroup.gzoom.infrastructure.uom.util.UomDtoUomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING
        , uses = {WorkEffortMeasureDtoGoalIndicatorMapper.class, WorkEffortDtoGoalMapper.class, UomDtoUomMapper.class}
        , imports = {Instant.class, Double.class, BigDecimal.class, Boolean.class})
public interface AcctgTransEntryDtoGoalValueMapper {
    List<GoalValue> acctgTransEntryListToGoalValueList(List<AcctgTransEntryDto> acctgTransAndEntryDtoList);

    @Mapping(target = "scorekpi", source = "scorekpi")
    @Mapping(target = "entryId", source = "acctgTransEntrySeqId")
    @Mapping(target = "goal", source = "workEffortMeasure.workEffort")
    //@Mapping(target = "ratingScale", source = "workEffortMeasRatScDto")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "origAmount", source = "origAmount")
    @Mapping(target = "valueUomId", source = "currencyUomId")
    @Mapping(target = "goalIndicator.valueCode", source = "workEffortMeasRatScDto.uomCode")
    @Mapping(target = "goalIndicator.valueCodeLang", source = "workEffortMeasRatScDto.uomCodeLang")
    @Mapping(target = "goalIndicator.valueDescr", source = "workEffortMeasRatScDto.uomDescr")
    @Mapping(target = "goalIndicator.valueDescrLang", source = "workEffortMeasRatScDto.uomDescrLang")
    GoalValue acctgTransEntryToGoalValue(AcctgTransEntryDto acctgTransEntryDto);

}
