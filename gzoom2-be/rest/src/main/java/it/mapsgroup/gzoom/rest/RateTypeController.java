package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.RateType;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssignmentRateEx;
import it.mapsgroup.gzoom.service.RateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "rate-types", produces = { MediaType.APPLICATION_JSON_VALUE })
public class RateTypeController {

    private final RateTypeService rateTypeService;

    @Autowired
    public RateTypeController( RateTypeService rateTypeService) {
        this.rateTypeService = rateTypeService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public Result<RateType> getRateTypes() {
        return Exec.exec("rate-types get", () -> rateTypeService.getRateTypes());
    }

    @RequestMapping(value = "rate-types-work-effort-id/{workEffortId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<WorkEffortAssignmentRateEx> getRateTypesWorkEffortId(@PathVariable(value = "workEffortId") String workEffortId) {
        return Exec.exec("rate-types-work-effort-id get", () -> rateTypeService.getRateTypesWorkEffortId(workEffortId));
    }
}
