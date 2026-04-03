package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.gateway.orgchart;

import it.mapsgroup.gzoom.common.LanguageType;
import it.mapsgroup.gzoom.entity.goalfile.model.ShowUOCode;
import it.mapsgroup.gzoom.entity.orgchart.gateway.OrganizationUnitRepositoryGateway;
import it.mapsgroup.gzoom.entity.orgchart.model.OrganizationUnit;
import it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper.orgchart.OrganizationUnitMapper;
import it.mapsgroup.gzoom.infrastructure.orgchart.util.PartyDtoOrganizationUnitMapper;
import it.mapsgroup.gzoom.entity.user.model.PermissionView;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;

@Slf4j
public class OrganizationUnitMyBatisRepositoryGateway implements OrganizationUnitRepositoryGateway {

    private final OrganizationUnitMapper organizationUnitMapper; // va sul db
    private final PartyDtoOrganizationUnitMapper partyDtoOrganizationUnitMapper; // mappa dto

    public OrganizationUnitMyBatisRepositoryGateway(OrganizationUnitMapper organizationUnitMapper
            , PartyDtoOrganizationUnitMapper partyDtoOrganizationUnitMapper) {
        this.organizationUnitMapper = organizationUnitMapper;
        this.partyDtoOrganizationUnitMapper = partyDtoOrganizationUnitMapper;
    }

    @Override
    public List<OrganizationUnit> getOrganizationUnitsAndTypes(String goalTypeId,
                                                               PermissionView permissionView,
                                                               Instant refDate,
                                                               String organizationId,
                                                               ShowUOCode showUoCode,
                                                               LanguageType languageType,
                                                               Boolean secondaryLang) {
        return partyDtoOrganizationUnitMapper.partyDtoListToOrganizationUnitList(this.organizationUnitMapper.selectOrganizationUnitsAndTypes(goalTypeId, permissionView, refDate, organizationId, showUoCode, languageType, secondaryLang));
    }

    @Override
    public List<OrganizationUnit> getOrganizationUnits(PermissionView permissionView,
                                                       String organizationId,
                                                       String roleTypeId,
                                                       Instant refDate,
                                                       Integer startYear,
                                                       Integer endYear,
                                                       ShowUOCode showUoCode,
                                                       LanguageType languageType,
                                                       Boolean secondaryLang) {
        return partyDtoOrganizationUnitMapper.partyDtoListToOrganizationUnitList(
                this.organizationUnitMapper.selectOrganizationUnits(permissionView, organizationId, roleTypeId, refDate, startYear,
                        endYear,showUoCode,
                        languageType, secondaryLang));
    }

    @Override
    public List<OrganizationUnit> getSupervisorOrganizationUnits(String organizationId, ShowUOCode showUoCode, LanguageType languageType, Boolean secondaryLang) {
        return partyDtoOrganizationUnitMapper.partyDtoListToOrganizationUnitList(this.organizationUnitMapper.selectSupervisorOrganizationUnits(organizationId, showUoCode, languageType, secondaryLang));
    }
}
