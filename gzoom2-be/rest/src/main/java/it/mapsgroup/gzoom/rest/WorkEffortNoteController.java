package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.commons.InfoPage;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.WorkEffortNoteExNoteData;
import it.mapsgroup.gzoom.service.WorkEffortNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "work-effort-note", produces = { MediaType.APPLICATION_JSON_VALUE })
public class WorkEffortNoteController {

    private final WorkEffortNoteService workEffortNoteService;

    @Autowired
    public WorkEffortNoteController(WorkEffortNoteService workEffortNoteService) {
        this.workEffortNoteService = workEffortNoteService;
    }

    @RequestMapping(value = "/total", method = RequestMethod.POST)
    @ResponseBody
    public int getWorkEffortNoteExNoteDataListPaginationTotal(@RequestBody InfoPage infoPage){
        return Exec.exec("getworkEffortNoteExNoteDataList-pagination", () -> this.workEffortNoteService.getTotale(infoPage));
    }

    @RequestMapping(value = "/pagination", method = RequestMethod.POST)
    @ResponseBody
    public Result<WorkEffortNoteExNoteData>getWorkEffortNoteExNoteDataListPagination(@RequestBody InfoPage infoPage){
        return Exec.exec("getworkEffortNoteExNoteDataList-pagination", () -> this.workEffortNoteService.getWorkEffortNoteExNoteDataListPagination(infoPage));
    }

    @PostMapping
    @ResponseBody
    public WorkEffortNoteExNoteData createWorkEffortNoteExNoteData(@RequestBody WorkEffortNoteExNoteData req){
        return Exec.exec("create workEffortNoteExNoteData", () -> this.workEffortNoteService.createWorkEffortNoteExNoteData(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateWorkEffortNoteExNoteData(@RequestBody WorkEffortNoteExNoteData req){
        return Exec.exec("update workEffortNoteExNoteData", () -> this.workEffortNoteService.updateWorkEffortNoteExNoteData(req) );
    }

    @DeleteMapping(value = "/{workEffortId}/{noteId}")
    @ResponseBody
    public boolean deleteWorkEffortNoteExNoteData(@PathVariable(value = "workEffortId") String workEffortId, @PathVariable(value = "noteId") String noteId ){
        return Exec.exec("delete workEffortNoteExNoteData", () -> this.workEffortNoteService.deleteWorkEffortNoteExNoteData(workEffortId, noteId) );
    }

}
