package it.mapsgroup.gzoom.infrastructure.goalfile.util;

import it.mapsgroup.gzoom.entity.goalfile.model.GoalIndicator;
import it.mapsgroup.gzoom.entity.goalfile.model.Indicator;
import it.mapsgroup.gzoom.entity.goalfile.model.RatingScale;
import it.mapsgroup.gzoom.entity.uom.PeriodType;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.GlAccountDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.GlAccountMeasRatScDto;
import it.mapsgroup.gzoom.infrastructure.goalfile.dto.WorkEffortMeasureDto;
import it.mapsgroup.gzoom.infrastructure.uom.dto.PeriodTypeDto;
import it.mapsgroup.gzoom.infrastructure.uom.util.UomDtoUomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING
        , uses = {WorkEffortDtoGoalMapper.class, UomDtoUomMapper.class, AcctgTransEntryDtoGoalValueMapper.class}
        , imports = {Instant.class, Double.class, BigDecimal.class, Boolean.class})
public interface WorkEffortMeasureDtoGoalIndicatorMapper {
    @Mapping(target = "glAccountId", source = "id")
    @Mapping(target = "weOtherGoalEnumId", source = "weOtherGoalEnumId", defaultExpression = "java(\"Y\".equals(indicator.getDetectOrgUnitIdFlag()) ? \"WEMOMG_ORG\" : \"WEMOMG_WEFF\")")
    GlAccountDto indicatorToGlAccountDto(Indicator indicator);

    @Mapping(target = "id", source = "glAccountId")
    @Mapping(target = "uom", source = "defaultUom")
    Indicator glAccountDtoToIndicator(GlAccountDto glAccountDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "indicator", source = ".")
    @Mapping(target="posted", constant = "false")
    @Mapping(target = "ratingScales", source = "glAccountDto.glAccountMeasRatScs")
    GoalIndicator glAccountDtoToGoalIndicator(GlAccountDto glAccountDto);

    List<RatingScale> glAccountMeasRatScDtoListToRatingScaleList(List<GlAccountMeasRatScDto> glAccountMeasRatScDtoList);

    @Mapping(target = "ratingValue", source = "uomRatingValue")
    @Mapping(target = "id", source = "uomId")
    @Mapping(target = "code", source = "uomCode")
    @Mapping(target = "codeLang", source = "uomCodeLang")
    @Mapping(target = "description", source = "uomDescr")
    @Mapping(target = "descriptionLang", source = "uomDescrLang")
    RatingScale glAccountMeasRatScDtoToRatingScale(GlAccountMeasRatScDto glAccountMeasRatScDto);

    @Mapping(target = "periodTypeId", source = "id")
    PeriodTypeDto periodTypeToPeriodTypeDto(PeriodType periodType);

    List<GoalIndicator> workEffortMeasureListToGoalIndicatorList(List<WorkEffortMeasureDto> goalMeasureList);

    @Mapping(target = "id", source = "workEffortMeasureId")
    @Mapping(target = "description", source = "uomDescr")
    @Mapping(target = "descriptionLang", source = "uomDescrLang")
    @Mapping(target = "accountName", source = "account.accountName")
    @Mapping(target = "accountNameLang", source = "account.accountNameLang")
    @Mapping(target = "accountCode", source = "account.accountCode")
    @Mapping(target = "uomRatingValue", source = "workEffortMeasRatSc.uomRatingValue")
    @Mapping(target = "value", source = "acctgTransEntry")
    @Mapping(target = "source", source = "account.source")
    @Mapping(target = "sourceLang", source = "account.sourceLang")
    GoalIndicator workEffortMeasureToGoalIndicator(WorkEffortMeasureDto goalMeasure);
}
