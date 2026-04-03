package it.mapsgroup.gzoom.infrastructure.uom.util;

import it.mapsgroup.gzoom.entity.uom.PeriodType;
import it.mapsgroup.gzoom.entity.uom.Uom;
import it.mapsgroup.gzoom.entity.uom.UomType;
import it.mapsgroup.gzoom.infrastructure.uom.dto.PeriodTypeDto;
import it.mapsgroup.gzoom.infrastructure.uom.dto.UomDto;
import it.mapsgroup.gzoom.infrastructure.uom.dto.UomTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.math.BigDecimal;
import java.time.Instant;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING
        , imports = {Instant.class, Double.class, BigDecimal.class})
public interface UomDtoUomMapper {
    @Mapping(target = "id", source = "uomId")
    @Mapping(target = "type", source = "uomType")
    Uom uomDtoToUom(UomDto uomDto);

    @Mapping(target = "id", source = "uomTypeId")
    UomType uomTypeDtoToUomType(UomTypeDto uomDto);

    @Mapping(target = "id", source = "periodTypeId")
    PeriodType periodTypeDtoToPeriodType(PeriodTypeDto uomDto);
}
