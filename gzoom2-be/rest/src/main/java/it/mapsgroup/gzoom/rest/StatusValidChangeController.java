package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.StatusValidChange;
import it.mapsgroup.gzoom.service.StatusValidChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "status-valid-change", produces = { MediaType.APPLICATION_JSON_VALUE })
public class StatusValidChangeController {
    private final StatusValidChangeService statusValidChangeService;

    @Autowired
    public StatusValidChangeController(StatusValidChangeService statusValidChangeService) {
        this.statusValidChangeService = statusValidChangeService;
    }

    @GetMapping("/status-type-id/{statusTypeId}")
    @ResponseBody
    public Result<StatusValidChange> getStatusItemList(@PathVariable String statusTypeId){
        return Exec.exec("get status valid change list by status type id", () -> this.statusValidChangeService.getStatusValidChangeByStatusTypeId(statusTypeId));
    }

    @PostMapping("/{statusTypeId}")
    @ResponseBody
    public boolean createStatusValidChange(@RequestBody StatusValidChange req, @PathVariable String statusTypeId){
        return Exec.exec("create status-valid-change", () -> this.statusValidChangeService.createStatusValidChange(req, statusTypeId) );
    }

    @PutMapping("/{statusTypeId}")
    @ResponseBody
    public boolean updateStatusValidChange(@RequestBody StatusValidChange req, @PathVariable String statusTypeId){
        return Exec.exec("update status-valid-change", () -> this.statusValidChangeService.updateStatusValidChange(req, statusTypeId) );
    }

    @DeleteMapping(value = "/{statusId}/{statusIdTo}")
    @ResponseBody
    public boolean deleteStatusValidChange(@PathVariable String statusId, @PathVariable String statusIdTo ){
        return Exec.exec("delete status-valid-change list", () -> this.statusValidChangeService.deleteStatusValidChange(statusId, statusIdTo) );
    }

}