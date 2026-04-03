package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.PeriodType;
import it.mapsgroup.gzoom.service.PeriodTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "periodType", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PeriodTypeController {

    private final PeriodTypeService periodTypeService;

    @Autowired
    public PeriodTypeController(PeriodTypeService periodTypeService) {
        this.periodTypeService = periodTypeService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Result<PeriodType> getPeriodTypes() {
        return Exec.exec("period-type get", periodTypeService::getPeriodTypes);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public boolean updatePeriodType(@RequestBody PeriodType periodType) {
        return Exec.exec("period-type update", () -> periodTypeService.updatePeriodType(periodType));
    }

    @RequestMapping(value = "/{periodTypes}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deletePeriodType(@PathVariable(value = "periodTypes") String[] periodTypes) {
        return Exec.exec("period-type delete", () -> periodTypeService.deletePeriodType(periodTypes));

    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public String createPeriodType(@RequestBody PeriodType periodType) {
        return Exec.exec("period-type post", () -> periodTypeService.createPeriodType(periodType));
    }

}
