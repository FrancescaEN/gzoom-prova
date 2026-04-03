package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.EmplPositionType;
import it.mapsgroup.gzoom.service.EmplPositionTypeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "emplPositionType", produces = { MediaType.APPLICATION_JSON_VALUE })
public class EmplPositionTypeController {

    private final EmplPositionTypeService emplPositionTypeService;

    public EmplPositionTypeController(EmplPositionTypeService emplPositionTypeService) {
        this.emplPositionTypeService = emplPositionTypeService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Result<EmplPositionType> getEmplPositionTypes() {
        return Exec.exec("empl-position-type get", emplPositionTypeService::getEmplPositionTypes);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public boolean updateEmplPositionType(@RequestBody EmplPositionType emplPositionType) {
        return Exec.exec("empl-position-type update", () -> emplPositionTypeService.updateEmplPositionType(emplPositionType));
    }

    @RequestMapping(value = "/{emplPositionTypes}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteEmplPositionType(@PathVariable(value = "emplPositionTypes") String[] emplPositionTypes) {
        return Exec.exec("empl-position-type delete", () -> emplPositionTypeService.deleteEmplPositionType(emplPositionTypes));

    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public String createEmplPositionType(@RequestBody EmplPositionType emplPositionType) {
        return Exec.exec("empl-position-type post", () -> emplPositionTypeService.createEmplPositionType(emplPositionType));
    }
}
