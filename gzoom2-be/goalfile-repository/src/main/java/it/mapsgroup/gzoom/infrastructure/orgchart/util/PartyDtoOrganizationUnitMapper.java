package it.mapsgroup.gzoom.infrastructure.orgchart.util;

import it.mapsgroup.gzoom.entity.orgchart.model.OrganizationUnit;
import it.mapsgroup.gzoom.infrastructure.goalfile.util.*;
import it.mapsgroup.gzoom.infrastructure.orgchart.dto.PartyDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {RoleTypeDtoOrganizationUnitTypeMapper.class}
)
public interface PartyDtoOrganizationUnitMapper {
    List<OrganizationUnit> partyDtoListToOrganizationUnitList(List<PartyDto> organizzationUnitList);

    @Mapping(source = "partyId", target = "id")
    @Mapping(source = "externalId", target = "otherCode")
    @Mapping(source = "partyName", target = "name")
    @Mapping(source = "partyNameLang", target = "nameLang")
    @Mapping(source = "parentRoleCode", target = "mainCode")
    @Mapping(source = "responsible.partyName", target = "responsible.description")
    @Mapping(source = "roleType", target = "organizationUnitType")
    OrganizationUnit partyDtoToOrganizationUnit(PartyDto orgUnitParty);

    @Mapping(source = "id", target = "partyId")
    @Mapping(source = "mainCode", target = "parentRoleCode")
    @Mapping(source = "otherCode", target = "externalId")
    @Mapping(source = "name", target = "partyName")
    @Mapping(source = "nameLang", target = "partyNameLang")
    @Mapping(source = "organizationUnitType", target = "roleType")
    @Mapping(source = "responsible", target = "responsible")
    PartyDto organizationUnitToPartyDto(OrganizationUnit orgUnit);
}
