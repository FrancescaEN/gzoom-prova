package it.mapsgroup.gzoom.infrastructure.orgchart.util;

import it.mapsgroup.gzoom.entity.orgchart.model.OrganizationUnitType;
import it.mapsgroup.gzoom.infrastructure.orgchart.dto.RoleTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleTypeDtoOrganizationUnitTypeMapper {
    @Mapping(source = "roleTypeId", target = "id")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "descriptionLang", target = "descriptionLang")
    OrganizationUnitType roleTypeDtoToOrganizationUnitType(RoleTypeDto orgUnitRoleType);

    @Mapping(target = "roleTypeId", source = "id")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "descriptionLang", source = "descriptionLang")
    RoleTypeDto organizationUnitTypeToRoleTypeDto(OrganizationUnitType orgUnitType);
}
