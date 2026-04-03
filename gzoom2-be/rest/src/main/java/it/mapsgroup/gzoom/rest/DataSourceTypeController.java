package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.DataSourceType;
import it.mapsgroup.gzoom.service.DataSourceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "data-source-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class DataSourceTypeController {
    private final DataSourceTypeService dataResourceTypeService;

    @Autowired
    public DataSourceTypeController(DataSourceTypeService dataResourceTypeService) {
        this.dataResourceTypeService = dataResourceTypeService;
    }

    @GetMapping
    @ResponseBody
    public Result<DataSourceType> getDataSourceType(){
        return Exec.exec("get data-source-type", this.dataResourceTypeService::getDataSourceType);
    }

    @PostMapping
    @ResponseBody
    public boolean createDataSourceType(@RequestBody DataSourceType req){
        return Exec.exec("create data-source-type", () -> this.dataResourceTypeService.createDataSourceType(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateDataSourceType(@RequestBody DataSourceType req){
        return Exec.exec("update data-source-type", () -> this.dataResourceTypeService.updateDataSourceType(req) );
    }

    @DeleteMapping(value = "/{dataResourceTypeId}")
    @ResponseBody
    public boolean deleteDataSourceType(@PathVariable(value = "dataResourceTypeId") String id){
        return Exec.exec("delete data-source-type", () -> this.dataResourceTypeService.deleteDataSourceType(id) );
    }

}
