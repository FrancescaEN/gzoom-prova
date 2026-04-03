package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.HeaderPortalPage;
import it.mapsgroup.gzoom.mybatis.dto.Party;
import it.mapsgroup.gzoom.mybatis.dto.PartyEx;
import it.mapsgroup.gzoom.mybatis.dto.Person;
import it.mapsgroup.gzoom.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static it.mapsgroup.gzoom.security.Principals.principal;


/**
 */
@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PartyController {

    private final PartyService partyService;

    @Autowired
    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @RequestMapping(value = "party/person", method = RequestMethod.GET)
    @ResponseBody
    public Result<Person> getPersons() {
        return Exec.exec("party/person get", partyService::getPersons);
    }

    @RequestMapping(value = "party/partiesExposed", method = RequestMethod.GET)
    @ResponseBody
    public Result<it.mapsgroup.gzoom.model.Person> getPartiesExposed() {
        return Exec.exec("party get", partyService::getPartiesExposed);
    }

    @RequestMapping(value = "party/{parentTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Party> getPartys(@PathVariable(value = "parentTypeId") String parentTypeId) {
        return Exec.exec("party get", () -> partyService.getPartys(principal().getUserLoginId(), parentTypeId));
    }

    @RequestMapping(value = "party/role-type/{roleTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Party> getPartyByRoleTypeId(@PathVariable String roleTypeId) {
        return Exec.exec("party get by roleTypeId", () -> this.partyService.getPartyByRoleTypeId(roleTypeId));
    }

    @RequestMapping(value = "party/role-type/{roleTypeId}/{glAccountId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Party> getByRoleTypeIdAndNotInGlAccount(@PathVariable String roleTypeId, @PathVariable String glAccountId) {
        return Exec.exec("party get By RoleTypeId And Not In GlAccount", () -> this.partyService.getByRoleTypeIdAndNotInGlAccount(roleTypeId, glAccountId));
    }

    @RequestMapping(value = "party/role-type/{roleTypeId}/current-organization", method = RequestMethod.GET)
    @ResponseBody
    public Result<Party> getPartiesByRoleTypeIdAndCurrentOrganizationId(@PathVariable String roleTypeId) {
        return Exec.exec("party get By RoleTypeId And current OrganizationID", () -> this.partyService.getPartiesByRoleTypeIdAndCurrentOrganizationId(roleTypeId));
    }


    @RequestMapping(value = "orgUnits/{parentTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<PartyEx> getOrgUnits(@PathVariable(value = "parentTypeId") String parentTypeId,
    @RequestParam Map<String,String> requestParams) {

        String roleTypeId = requestParams.get("roleTypeId");
        String workEffortTypeId = requestParams.get("workEffortTypeId");
        String company = requestParams.get("company");
        return Exec.exec("orgUnit get", () -> partyService.getOrgUnits(principal().getUserLoginId(), parentTypeId, roleTypeId, workEffortTypeId, company));
    }

    @RequestMapping(value = "uo-gestore/{context}/{organizationId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<PartyEx> getUOGestore(@PathVariable String context, @PathVariable String organizationId) {

        return Exec.exec("UOGestore get", () -> partyService.getUOGestore(context, organizationId));
    }

    @RequestMapping(value = "uo-gestore/{context}/resp-center-role-type/{respCenterRoleTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<PartyEx> getUOGestoreByRespCenterRoleTypeId(@PathVariable String context, @PathVariable String respCenterRoleTypeId) {

        return Exec.exec("UOGestore By RespCenterRoleTypeId", () -> partyService.getUOGestoreByRespCenterRoleTypeId(context, respCenterRoleTypeId));
    }

    @RequestMapping(value = "party/partyByOrgId", method = RequestMethod.GET)
    @ResponseBody
    public Result<Party> getPartyByOrgId() {
        return Exec.exec("partyByOrgId get", () -> partyService.getPartyByOrganizationId(principal().getUserLoginId()));
    }

    @RequestMapping(value = "party/managers", method = RequestMethod.GET)
    @ResponseBody
    public Result<Party> getManagers() {
        return Exec.exec("getManagers", partyService::getManagers);
    }
    
    @RequestMapping(value = "party/roleType/{roleTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Party> getRoleTypePartys(@PathVariable(value = "roleTypeId") String roleTypeId,
    @RequestParam Map<String,String> requestParams) {
        String roleTypeIdFrom = requestParams.get("roleTypeIdFrom");
        String workEffortTypeId = requestParams.get("workEffortTypeId");
        return Exec.exec("party get", () -> partyService.getRoleTypePartys(roleTypeId,roleTypeIdFrom,workEffortTypeId));
    }

    @GetMapping("party/roleType/between/{roleTypeId}")
    @ResponseBody
    public Result<Party> getRoleTypePartysBetween(@PathVariable(value = "roleTypeId") String roleTypeId) {
        return Exec.exec("party get between", () -> partyService.getRoleTypePartysBetween(roleTypeId));
    }

    @RequestMapping(value = "party/headerPortalPage", method = RequestMethod.GET)
    @ResponseBody
    public HeaderPortalPage getHeaderPortalPage() {
        return Exec.exec("party getHeaderPortalPage", partyService::getHeaderPortalPage);
    }

}
