package it.mapsgroup.gzoom.entity.orgchart.gateway;

import it.mapsgroup.gzoom.common.LanguageType;
import it.mapsgroup.gzoom.entity.orgchart.model.OrganizationUnit;
import it.mapsgroup.gzoom.entity.goalfile.model.ShowUOCode;
import it.mapsgroup.gzoom.entity.user.model.PermissionView;

import java.time.Instant;

import java.util.List;

public interface OrganizationUnitRepositoryGateway {
    List<OrganizationUnit> getOrganizationUnitsAndTypes(String goalTypeId,
                                                        PermissionView permissionView,
                                                        Instant refDate, String organizationId,
                                                        ShowUOCode showUoCode,
                                                        LanguageType languageType,
                                                        Boolean secondaryLang);

    List<OrganizationUnit> getOrganizationUnits(PermissionView permissionView, String organizationId, String roleTypeId,
                                                Instant refDate, Integer startYear, Integer endYear, ShowUOCode showUoCode,
                                                LanguageType languageType, Boolean secondaryLang);
    List<OrganizationUnit> getSupervisorOrganizationUnits(String organizationId, ShowUOCode showUoCode,
                                                          LanguageType languageType, Boolean secondaryLang);

}
