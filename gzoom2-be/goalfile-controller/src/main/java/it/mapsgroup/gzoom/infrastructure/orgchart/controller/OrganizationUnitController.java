package it.mapsgroup.gzoom.infrastructure.orgchart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.mapsgroup.gzoom.entity.goalfile.model.Context;
import it.mapsgroup.gzoom.entity.goalfile.model.ShowUOCode;
import it.mapsgroup.gzoom.entity.orgchart.model.OrganizationUnit;
import it.mapsgroup.gzoom.exception.ResourceNotFoundException;
import it.mapsgroup.gzoom.orgchart.OrganizationUnitUseCase;
import it.mapsgroup.gzoom.user.PermissionUseCase;
import it.mapsgroup.gzoom.orgchart.exception.OrganizationUnitNotFoundException;
import it.mapsgroup.gzoom.user.exception.PermissionNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class OrganizationUnitController {
    private final OrganizationUnitUseCase organizationUnitUseCase;
    private final PermissionUseCase permissionUseCase;

    public OrganizationUnitController(OrganizationUnitUseCase organizationUnitUseCase,
                                      PermissionUseCase permissionUseCase) {
        this.organizationUnitUseCase = organizationUnitUseCase;
        this.permissionUseCase = permissionUseCase;
    }

    @GetMapping("/v1/goal/type/{goalTypeId}/organization-units")
    @Operation(summary = "Restituisce tutte le unità responsabili in base alle abilitazioni dell'utente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "404", description = "Nessuna unità responsabile")
    })
    public List<OrganizationUnit> getOrganizationUnitsAndTypes(@PathVariable String goalTypeId,
                                                               @RequestParam Context context,
                                                               @RequestParam Instant referenceDate,
                                                               @RequestParam(required = false, defaultValue = "NONE") ShowUOCode showUoCode,
                                                               @RequestParam(required = false) Boolean secondaryLang) {
        try {
            String userLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
            return this.organizationUnitUseCase.getOrganizationUnitsAndTypes(userLoginId,
                    context,
                    referenceDate,
                    goalTypeId,
                    showUoCode, secondaryLang);
        } catch (OrganizationUnitNotFoundException | PermissionNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/v1/organization-units")
    @Operation(summary = "Restituisce tutte le unità responsabili")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "404", description = "Nessuna unità responsabili")
    })
    public List<OrganizationUnit> getOrganizationUnits(@RequestParam Context context,
                                                       @RequestParam(required = false) String organizationUnitTypeId,
                                                       @RequestParam(required = false) Instant refDate,
                                                       @RequestParam(required = false) Integer startYear,
                                                       @RequestParam(required = false) Integer endYear,
                                                       @RequestParam(required = false, defaultValue = "NONE") ShowUOCode showUoCode,
                                                       @RequestParam(required = false) Boolean secondaryLang) {
        try {
            String userLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
            return this.organizationUnitUseCase.getOrganizationUnits(userLoginId, context,
                    organizationUnitTypeId, refDate, startYear, endYear, showUoCode, secondaryLang);
        } catch (OrganizationUnitNotFoundException | PermissionNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @GetMapping("/v1/supervisor-organization-units")
    @Operation(summary = "Restituisce tutte le unità responsabili superiori")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successo"),
            @ApiResponse(responseCode = "204", description = "Nessuna unità responsabile superiore")
    })
    public List<OrganizationUnit> getSupervisorOrganizationUnits(@RequestParam Context context,
                                                                 @RequestParam(required = false, defaultValue = "NONE") ShowUOCode showUoCode,
                                                                 @RequestParam(required = false) Boolean secondaryLang) {
        try {
            var userLoginId = SecurityContextHolder.getContext().getAuthentication().getName();
            var permissionView = permissionUseCase.getPermission(userLoginId, context);
            return this.organizationUnitUseCase.getSupervisorOrganizationUnits(permissionView.getOrganizationId(), showUoCode,
                    secondaryLang);
        } catch (OrganizationUnitNotFoundException | PermissionNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
}
