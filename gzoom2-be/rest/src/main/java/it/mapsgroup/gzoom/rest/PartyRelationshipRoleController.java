package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.PartyRelationshipRole;
import it.mapsgroup.gzoom.service.PartyRelationshipRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "party-relationship-role", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PartyRelationshipRoleController {
    private final PartyRelationshipRoleService partyRelationshipRoleService;

    @Autowired
    public PartyRelationshipRoleController(PartyRelationshipRoleService partyRelationshipRoleService) {
        this.partyRelationshipRoleService = partyRelationshipRoleService;
    }

    @GetMapping(value = "/{partyRelationshipTypeId}")
    @ResponseBody
    public Result<PartyRelationshipRole> findByPartyRelationshipTypeId(@PathVariable("partyRelationshipTypeId") String partyRelationshipTypeId){
        return Exec.exec("find partyRelationshipRole by partyRelationshipTypeId", () -> this.partyRelationshipRoleService.findByPartyRelationshipTypeId(partyRelationshipTypeId));
    }

    @PostMapping
    @ResponseBody
    public boolean create(@RequestBody PartyRelationshipRole partyRelationshipRole){
        return Exec.exec("create partyRelationshipRole", () -> this.partyRelationshipRoleService.create(partyRelationshipRole));
    }

    @PutMapping
    @ResponseBody
    public boolean update(@RequestBody PartyRelationshipRole partyRelationshipRole){
        return Exec.exec("update partyRelationshipRole", () -> this.partyRelationshipRoleService.update(partyRelationshipRole) );
    }

    @DeleteMapping(value = "/{partyRelationshipTypeId}/{roleTypeValidFrom}/{roleTypeValidTo}")
    @ResponseBody
    public boolean delete(@PathVariable("partyRelationshipTypeId") String partyRelationshipTypeId, @PathVariable("roleTypeValidFrom") String roleTypeValidFrom, @PathVariable("roleTypeValidTo") String roleTypeValidTo){
        return Exec.exec("delete partyRelationshipRole", () -> this.partyRelationshipRoleService.delete(partyRelationshipTypeId, roleTypeValidFrom, roleTypeValidTo));
    }
}
