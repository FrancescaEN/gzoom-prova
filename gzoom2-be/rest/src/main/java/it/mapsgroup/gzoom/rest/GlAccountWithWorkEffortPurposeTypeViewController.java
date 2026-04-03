package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.Filter;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountWithWorkEffortPurposeTypeView;
import it.mapsgroup.gzoom.service.GlAccountWithWorkEffortPurposeTypeViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "ga-wept-view", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlAccountWithWorkEffortPurposeTypeViewController {
    private final GlAccountWithWorkEffortPurposeTypeViewService glAccountWithWorkEffortPurposeTypeViewService;

    @Autowired
    public GlAccountWithWorkEffortPurposeTypeViewController(GlAccountWithWorkEffortPurposeTypeViewService workEffortViewService) {
        this.glAccountWithWorkEffortPurposeTypeViewService = workEffortViewService;
    }

    @GetMapping(value = "/{organizationId}")
    @ResponseBody
    public Result<GlAccountWithWorkEffortPurposeTypeView> getGlAccountWithWorkEffortPurposeTypeView(@PathVariable(value = "organizationId") String organizationId){
        return Exec.exec("get ga-wept-view", () -> this.glAccountWithWorkEffortPurposeTypeViewService.getGlAccountWithWorkEffortPurposeTypeView(organizationId) );
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    @ResponseBody
    public Result<GlAccountWithWorkEffortPurposeTypeView> getGlAccountWithWorkEffortPurposeTypeViewFilter(@RequestBody Filter filter){
        return Exec.exec("get  ga-wept-view-filter", () -> this.glAccountWithWorkEffortPurposeTypeViewService.getGlAccountWithWorkEffortPurposeTypeViewFilter(filter) );
    }
}
