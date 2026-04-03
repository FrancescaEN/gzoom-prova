package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.Enumeration;
import it.mapsgroup.gzoom.service.EnumerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "enumeration", produces = { MediaType.APPLICATION_JSON_VALUE })
public class EnumerationController {

    private final EnumerationService enumerationService;

    @Autowired
    public EnumerationController(EnumerationService enumerationService) {
        this.enumerationService = enumerationService;
    }

    @RequestMapping(value = "/{enumTypeId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<Enumeration> getEnumeration(@PathVariable(value = "enumTypeId") String enumTypeId) {
        return Exec.exec("get enumeration",() -> enumerationService.getEnumerations(enumTypeId));
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    @ResponseBody
    public Result<Enumeration> getEnumerationFilter(@RequestBody Filter filter){
        return Exec.exec("get  enumeration-filter", () -> this.enumerationService.getEnumerationFilter(filter) );
    }

}
