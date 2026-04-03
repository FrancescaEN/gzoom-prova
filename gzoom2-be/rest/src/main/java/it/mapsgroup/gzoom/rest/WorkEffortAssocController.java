package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssoc;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortAssocEx;
import it.mapsgroup.gzoom.service.WorkEffortAssocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "work-effort-assoc", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortAssocController {
    private final WorkEffortAssocService workEffortAssocService;

    @Autowired
    public WorkEffortAssocController(WorkEffortAssocService workEffortAssocService) {
        this.workEffortAssocService = workEffortAssocService;
    }

    @GetMapping
    @ResponseBody
    public List<WorkEffortAssoc> getWorkEffortAssoc(){
        return Exec.exec("get work-effort-assoc", () -> this.workEffortAssocService.getWorkEffortAssocList());
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.POST)
    @ResponseBody
    public Result<WorkEffortAssocEx>getWorkEffortAssocPagination(@RequestBody InfoPage infoPage){
        return Exec.exec("get work-effort-assoc-pagination", () -> this.workEffortAssocService.getWorkEffortAssocPagination(infoPage) );
    }

    @RequestMapping(value = "/total", method = RequestMethod.POST)
    @ResponseBody
    public int getWorkEffortAssocPaginationTotal(@RequestBody InfoPage infoPage){
        return Exec.exec("get work-effort-assoc-pagination-total", () -> this.workEffortAssocService.getTotale(infoPage) );
    }

    @PostMapping
    @ResponseBody
    public boolean createWorkEffortAssoc(@RequestBody WorkEffortAssoc req){
        return Exec.exec("create work-effort-assoc", () -> this.workEffortAssocService.createWorkEffortAssoc(req) );
    }

    @PutMapping
    @ResponseBody
    public Integer updateWorkEffortAssoc(@RequestBody WorkEffortAssoc req){
        return Exec.exec("update work-effort-assoc", () -> this.workEffortAssocService.updateWorkEffortAssoc(req) );
    }

    @PostMapping(value = "delete/")
    @ResponseBody
    public Integer deleteWorkEffortAssoc(@RequestBody WorkEffortAssoc req ){
        return Exec.exec("delete work-effort-assoc", () -> this.workEffortAssocService.deleteWorkEffortAssoc(req) );
    }

}
