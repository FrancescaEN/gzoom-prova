package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.DataSource;
import it.mapsgroup.gzoom.mybatis.dto.DataSourceEx;
import it.mapsgroup.gzoom.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "data-source", produces = { MediaType.APPLICATION_JSON_VALUE })
public class DataSourceController {
    private final DataSourceService dataSourceService;

    @Autowired
    public DataSourceController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping(value = "/{dataSourceId}")
    @ResponseBody
    public DataSource getDataSourceById(@PathVariable String dataSourceId){
        return Exec.exec("get data-source", () ->  this.dataSourceService.getDataSourceById(dataSourceId));
    }

    @GetMapping(value = "/dataSource")
    @ResponseBody
    public Result<DataSource> getDataSource(){
        return Exec.exec("get data-source", this.dataSourceService::getDataSource);
    }

    @GetMapping(value = "/dataSourceEx")
    @ResponseBody
    public Result<DataSourceEx> getDataSourceEx(){
        return Exec.exec("get data-source-ex", this.dataSourceService::getDataSourceEx);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public DataSource createDataSource(@RequestBody DataSource req){
        return Exec.exec("create data-source", () -> this.dataSourceService.createDataSource(req) );
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public Boolean updateDataSource(@RequestBody  DataSource dataSource) {
        return Exec.exec("update data-source", () -> this.dataSourceService.updateDataSource(dataSource));
    }

    @DeleteMapping(value = "/{dataSourceId}")
    @ResponseBody
    public boolean deleteDataSource(@PathVariable(value = "dataSourceId") String id){
        return Exec.exec("delete data-source", () -> this.dataSourceService.deleteDataSource(id) );
    }
}
