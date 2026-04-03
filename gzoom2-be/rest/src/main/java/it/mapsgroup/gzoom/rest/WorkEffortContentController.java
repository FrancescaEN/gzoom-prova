package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContentEx;
import it.mapsgroup.gzoom.service.WorkEffortContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "work-effort-content", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortContentController {

    private final WorkEffortContentService workEffortContentService;

    @Autowired
    public WorkEffortContentController(WorkEffortContentService workEffortContentService) {
        this.workEffortContentService = workEffortContentService;
    }

    @GetMapping
    @ResponseBody
    public Result<WorkEffortContentEx> getWorkEffortContentExList(){
        return Exec.exec("get workEffortContentExList", this.workEffortContentService::getWorkEffortContentExList);
    }

    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    @ResponseBody
    public Result<WorkEffortContentEx> getWorkEffortContentListExFilter(@RequestBody InfoPage infoPage){
        return Exec.exec("get work-effort-content-list-filter", () -> this.workEffortContentService.getWorkEffortContentListExFilter(infoPage) );
    }

    @PostMapping
    @ResponseBody
    public WorkEffortContentEx createWorkEffortContentEx(@RequestBody WorkEffortContentEx req){
        return Exec.exec("create workEffortContentEx", () -> this.workEffortContentService.createWorkEffortContentEx(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateWorkEffortContentEx(@RequestBody WorkEffortContentEx req){
        return Exec.exec("update workEffortContentExContent", () -> this.workEffortContentService.updateWorkEffortContentEx(req) );
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public boolean deleteWorkEffortContentEx(@RequestBody WorkEffortContentEx req){
        return Exec.exec("delete workEffortContentEx", () -> this.workEffortContentService.deleteWorkEffortContentEx(req) );
    }
}
