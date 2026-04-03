package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.PartyRelationshipType;
import it.mapsgroup.gzoom.service.PartyRelationshipTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "party-relationship-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PartyRelationshipTypeController {

    private final PartyRelationshipTypeService partyRelationshipTypeService;

    @Autowired
    public PartyRelationshipTypeController(PartyRelationshipTypeService partyRelationshipTypeService) {
        this.partyRelationshipTypeService = partyRelationshipTypeService;
    }

    @GetMapping
    @ResponseBody
    public Result<PartyRelationshipType> findAll(){
        return Exec.exec("find all partyRelationshipType", this.partyRelationshipTypeService::findAllOrderById);
    }

    @PostMapping
    @ResponseBody
    public boolean create(@RequestBody PartyRelationshipType partyRelationshipType){
        return Exec.exec("create partyRelationshipType", () -> this.partyRelationshipTypeService.create(partyRelationshipType));
    }

    @PutMapping
    @ResponseBody
    public boolean update(@RequestBody PartyRelationshipType partyRelationshipType){
        return Exec.exec("update partyRelationshipType", () -> this.partyRelationshipTypeService.update(partyRelationshipType) );
    }

    @DeleteMapping(value = "/{partyRelationshipTypeId}")
    @ResponseBody
    public boolean delete(@PathVariable(value = "partyRelationshipTypeId") String[] id){
        return Exec.exec("delete partyRelationshipType", () -> this.partyRelationshipTypeService.delete(id));
    }
}
