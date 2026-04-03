package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.StatusType;
import it.mapsgroup.gzoom.service.StatusTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "status-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class StatusTypeController {
    private final StatusTypeService statusTypeService;

    @Autowired
    public StatusTypeController(StatusTypeService statusTypeService) {
        this.statusTypeService = statusTypeService;
    }

    @GetMapping
    @ResponseBody
    public Result<StatusType> getStatusTypeList(){
        return Exec.exec("get status type list", this.statusTypeService::getStatusTypeList);
    }


    @GetMapping(value = "/{statusTypeId}")
    @ResponseBody
    public StatusType getStatusTypeById(@PathVariable String statusTypeId){
        return Exec.exec("get status type by id", ()-> this.statusTypeService.getStatusTypeById(statusTypeId));
    }

    @PostMapping
    @ResponseBody
    public boolean createStatusType(@RequestBody StatusType req){
        return Exec.exec("create status-type", () -> this.statusTypeService.createStatusType(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateStatusType(@RequestBody StatusType req){
        return Exec.exec("update status-type", () -> this.statusTypeService.updateStatusType(req) );
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public boolean deleteStatusType(@PathVariable(value = "id") String[] id){
        return Exec.exec("delete status-type list", () -> this.statusTypeService.deleteStatusType(id) );
    }

}
