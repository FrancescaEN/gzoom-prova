package it.mapsgroup.gzoom.orgchart;

import it.mapsgroup.gzoom.configuration.spring.LanguageConfiguration;
import it.mapsgroup.gzoom.entity.goalfile.model.Context;
import it.mapsgroup.gzoom.entity.goalfile.model.ShowUOCode;
import it.mapsgroup.gzoom.entity.orgchart.gateway.OrganizationUnitRepositoryGateway;
import it.mapsgroup.gzoom.entity.orgchart.model.OrganizationUnit;
import it.mapsgroup.gzoom.entity.user.model.PermissionView;
import it.mapsgroup.gzoom.orgchart.exception.OrganizationUnitNotFoundException;
import it.mapsgroup.gzoom.user.exception.PermissionNotFoundException;
import it.mapsgroup.gzoom.user.PermissionUseCase;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;

@Slf4j
public class OrganizationUnitUseCase {

    private final OrganizationUnitRepositoryGateway organizationUnitRepositoryGateway;
    private final LanguageConfiguration languageConfiguration;
    private final PermissionUseCase permissionUseCase;

    public OrganizationUnitUseCase(OrganizationUnitRepositoryGateway organizationUnitRepositoryGateway,
                                   LanguageConfiguration languageConfiguration, PermissionUseCase permissionUseCase)  {
        this.organizationUnitRepositoryGateway = organizationUnitRepositoryGateway;
        this.languageConfiguration = languageConfiguration;
        this.permissionUseCase = permissionUseCase;
    }

    public List<OrganizationUnit> getOrganizationUnitsAndTypes(String userLoginId,
                                                               Context context,
                                                               Instant refDate,
                                                               String goalTypeId,
                                                               ShowUOCode showUoCode,
                                                               Boolean secondaryLang) throws OrganizationUnitNotFoundException, PermissionNotFoundException {
        PermissionView permissionView = this.permissionUseCase.getPermission(userLoginId, context);
        List<OrganizationUnit> result = this.organizationUnitRepositoryGateway.getOrganizationUnitsAndTypes(goalTypeId, permissionView, refDate,
                permissionView.getOrganizationId(), showUoCode, languageConfiguration.getLanguageType(), secondaryLang);
//        log.info("getOrganizationUnitsAndTypes result={}", result);
        if (result.isEmpty()) {
            throw new OrganizationUnitNotFoundException("No organizationUnit");
        }
        return result;
    }

    public List<OrganizationUnit> getOrganizationUnits(String userLoginId,
                                                       Context context,
                                                       String organizationUnitTypeId,
                                                       Instant refDate,
                                                       Integer startYear,
                                                       Integer endYear,
                                                       ShowUOCode showUoCode,
                                                       Boolean secondaryLang) throws OrganizationUnitNotFoundException, PermissionNotFoundException {
        PermissionView permissionView = this.permissionUseCase.getPermission(userLoginId, context);
        List<OrganizationUnit> result = this.organizationUnitRepositoryGateway.getOrganizationUnits(permissionView, permissionView.getOrganizationId(),
                organizationUnitTypeId, refDate, startYear, endYear, showUoCode, languageConfiguration.getLanguageType(), secondaryLang);
        log.info("getOrganizationUnits result={}", result);
        if (result.isEmpty()) {
            throw new OrganizationUnitNotFoundException("No organizationUnit");
        }
        return result;
    }

    public List<OrganizationUnit> getSupervisorOrganizationUnits(String organizationId, ShowUOCode showUoCode,
                                                                 Boolean secondaryLang) throws OrganizationUnitNotFoundException {
        List<OrganizationUnit> result = this.organizationUnitRepositoryGateway.getSupervisorOrganizationUnits(organizationId,
                showUoCode, languageConfiguration.getLanguageType(), secondaryLang);
        log.info("getSupervisorOrganizationUnits result={}", result);
        if (result.isEmpty()) {
            throw new OrganizationUnitNotFoundException("No organizationUnit");
        }
        return result;
    }
}
