package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.PartyType;
import it.mapsgroup.gzoom.service.PartyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "party-types", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PartyTypeController {

    private final PartyTypeService partyTypeService;

    @Autowired
    public PartyTypeController(PartyTypeService partyTypeService) {
        this.partyTypeService = partyTypeService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Result<PartyType> getPartyTypes() {
        return Exec.exec("party-types get", () -> partyTypeService.getPartyTypes());
    }
}
