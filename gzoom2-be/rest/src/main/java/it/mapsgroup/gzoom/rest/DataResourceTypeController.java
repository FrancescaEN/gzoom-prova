package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.DataResourceType;
import it.mapsgroup.gzoom.service.DataResourceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "data-resource-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class DataResourceTypeController {
    private final DataResourceTypeService dataResourceTypeService;

    @Autowired
    public DataResourceTypeController(DataResourceTypeService dataResourceTypeService) {
        this.dataResourceTypeService = dataResourceTypeService;
    }

    @GetMapping
    @ResponseBody
    public Result<DataResourceType> getDataResourceType(){
        return Exec.exec("get data-resource-type", this.dataResourceTypeService::getDataResourceType);
    }

    @PostMapping
    @ResponseBody
    public boolean createDataResourceType(@RequestBody DataResourceType req){
        return Exec.exec("create data-resource-type", () -> this.dataResourceTypeService.createDataResourceType(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateDataResourceType(@RequestBody DataResourceType req){
        return Exec.exec("update data-resource-type", () -> this.dataResourceTypeService.updateDataResourceType(req) );
    }

    @DeleteMapping(value = "/{dataResourceTypeId}")
    @ResponseBody
    public boolean deleteDataResourceType(@PathVariable(value = "dataResourceTypeId") String id){
        return Exec.exec("delete data-resource-type", () -> this.dataResourceTypeService.deleteDataResourceType(id) );
    }

}
