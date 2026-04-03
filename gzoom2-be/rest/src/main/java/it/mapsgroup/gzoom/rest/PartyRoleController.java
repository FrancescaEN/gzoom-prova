package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.PartyRole;
import it.mapsgroup.gzoom.mybatis.dto.PartyRoleEx;
import it.mapsgroup.gzoom.service.PartyRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static it.mapsgroup.gzoom.security.Principals.principal;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "party-role", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PartyRoleController {
    private final PartyRoleService partyRoleService;

    @Autowired
    public PartyRoleController(PartyRoleService partyRoleService) {
        this.partyRoleService = partyRoleService;
    }

    @GetMapping(value = "/")
    @ResponseBody
    public Result<PartyRoleEx> getPartyRoleOrgUnit(){
        return Exec.exec("get party-role-org-id", () -> this.partyRoleService.getPartyRoleOrgUnit(principal().getUserLoginId()));
    }

    @GetMapping(value = "/ex-role-type")
    @ResponseBody
    public Result<PartyRoleEx> getPartyRoleExOrgUnit(@RequestParam(required = false) String accountTypeEnumId) {
        return Exec.exec("get party-role-ex-role-type", () ->
                this.partyRoleService.getPartyRoleExOrgUnit(
                        principal().getUserLoginId(),
                        accountTypeEnumId
                )
        );
    }


    @GetMapping(value = "/all")
    @ResponseBody
    public Result<PartyRole> getPartyRole(
            @RequestParam(required = false) boolean isSecondaryLang,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String matchModeSearch,
            @RequestParam(required = false) String[] roleTypeId
    ){
        return Exec.exec("get party-role", () -> this.partyRoleService.getPartyRole( isSecondaryLang, search, matchModeSearch, roleTypeId));
    }

}

