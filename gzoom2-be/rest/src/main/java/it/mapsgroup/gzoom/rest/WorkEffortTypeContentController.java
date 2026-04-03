package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.service.WorkEffortTypeContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortTypeContentController {
    private final WorkEffortTypeContentService workEffortTypeContentService;

    @Autowired
    public WorkEffortTypeContentController(WorkEffortTypeContentService workEffortTypeContentService) {
        this.workEffortTypeContentService = workEffortTypeContentService;
    }

    @RequestMapping(value = "workEffortTypeContentParams", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> getWorkEffortTypeContentParams(@RequestParam Map<String,String> requestParams) {
        String workEffortTypeId = requestParams.get("workEffortTypeId");
        String contentId = requestParams.get("contentId");
        return Exec.exec("workEffortTypeContentParams get", () -> workEffortTypeContentService.getWorkEffortTypeContentParams(workEffortTypeId, contentId));
    }
}
