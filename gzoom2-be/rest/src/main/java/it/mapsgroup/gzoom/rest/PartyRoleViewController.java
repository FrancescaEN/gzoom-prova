package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.PartyRoleView;
import it.mapsgroup.gzoom.service.PartyRoleViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "party-role-view", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PartyRoleViewController {
    private final PartyRoleViewService partyRoleViewService;

    @Autowired
    public PartyRoleViewController(PartyRoleViewService partyRoleViewService) {
        this.partyRoleViewService = partyRoleViewService;
    }

    @GetMapping(value = "/{statusId}/{organizationId}")
    @ResponseBody
    public Result<PartyRoleView> getPartyRoleView(@PathVariable(value = "statusId") String statusId, @PathVariable(value = "organizationId") String organizationId){
        return Exec.exec("get party-role-view", () -> this.partyRoleViewService.getPartyRoleView(statusId, organizationId) );
    }

    @GetMapping(value = "/{statusId}/{organizationId}/{roleTypeId}")
    @ResponseBody
    public Result<PartyRoleView> getPartyRoleViewRoleTypeId(@PathVariable(value = "statusId") String statusId, @PathVariable(value = "organizationId") String organizationId, @PathVariable(value = "roleTypeId") String roleTypeId){
        return Exec.exec("get party-role-view-role-type-Id", () -> this.partyRoleViewService.getPartyRoleViewRoleTypeId(statusId, organizationId, roleTypeId) );
    }

    @GetMapping(value = "/{roleTypeId}")
    @ResponseBody
    public Result<PartyRoleView> getPartyRoleViewByRoleTypeId(@PathVariable String roleTypeId){
        return Exec.exec("get party-role-view-by-role-type-Id", () -> this.partyRoleViewService.getPartyRoleViewByRoleTypeId(roleTypeId) );
    }
}

