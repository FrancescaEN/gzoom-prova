package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.QueryConfig;
import it.mapsgroup.gzoom.service.QueryConfigService;
import it.mapsgroup.gzoom.service.QueryExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import static it.mapsgroup.gzoom.security.Principals.principal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class QueryConfigController {

    private final QueryConfigService queryConfigService;
    private final QueryExecutorService queryExecutorService;

    @Autowired
    public QueryConfigController(QueryConfigService queryConfigService, QueryExecutorService queryExecutorService) {
        this.queryConfigService = queryConfigService;
        this.queryExecutorService = queryExecutorService;
    }

    @RequestMapping(value = "query-config", method = RequestMethod.GET)
    @ResponseBody
    public Result<QueryConfig> getQueryConfig() {
        return Exec.exec("get query-config", queryConfigService::getQueryConfig);
    }

    @RequestMapping(value = "query-config/query-type/{queryType}", method = RequestMethod.GET)
    @ResponseBody
    public Result<QueryConfig> getQueryConfigByQueryType(@PathVariable("queryType") String queryType) {
        return Exec.exec("get query-config", () -> queryConfigService.getQueryConfigByQueryType(queryType));
    }

    @RequestMapping(value = "query-config/all", method = RequestMethod.GET)
    @ResponseBody
    public Result<QueryConfig> getAllQueryConfig() {
        return Exec.exec("get query-config",() -> queryConfigService.getAllQueryConfig(null,null, principal().getUserLoginId()));
    }

    @RequestMapping(value = "query-config/all/{parentTypeId}/{queryType}", method = RequestMethod.GET)
    @ResponseBody
    public Result<QueryConfig> getAllQueryConfigWithParent(@PathVariable(value = "parentTypeId") String parentTypeId,
                                                           @PathVariable(value = "queryType") String queryType) {
        return Exec.exec("get query-config",() -> queryConfigService.getAllQueryConfig(parentTypeId,queryType,principal().getUserLoginId()));
    }

    @RequestMapping(value = "query-config/id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public QueryConfig getQueryConfig(@PathVariable(value = "id") String id) {
        return Exec.exec("get query-config with id",() -> queryConfigService.getQueryConfig(id));
    }

    @RequestMapping(value = "query-config/exec", method = RequestMethod.POST)
    @ResponseBody
    public String executeQuery(@RequestBody QueryConfig query, HttpServletRequest req, HttpServletResponse response) {
        return Exec.exec("execute query-config with id",() -> queryExecutorService.execQuery(query, req, response, true));
    }

    @RequestMapping(value = "query-config/create", method = RequestMethod.POST)
    @ResponseBody
    public String createQueryConfig(@RequestBody QueryConfig req){
        return Exec.exec("create query-config", () -> this.queryConfigService.createQueryConfig(req) );
    }

    @RequestMapping(value = "query-config/update", method = RequestMethod.PUT)
    @ResponseBody
    public boolean updateQueryConfig(@RequestBody QueryConfig req){
        return Exec.exec("update query-config", () -> this.queryConfigService.updateQueryConfig(req) );
    }

    @RequestMapping(value = "query-config/update/info-base", method = RequestMethod.PUT)
    @ResponseBody
    public boolean updateQueryConfigInfoBase(@RequestBody QueryConfig req){
        return Exec.exec("update query-config info-base", () -> this.queryConfigService.updateQueryConfigInfoBase(req) );
    }

    @RequestMapping(value = "query-config/update/conditions", method = RequestMethod.PUT)
    @ResponseBody
    public boolean updateConditions(@RequestBody QueryConfig req){
        return Exec.exec("update query-config conditions", () -> this.queryConfigService.updateConditions(req) );
    }

    @RequestMapping(value = "query-config/delete/{queryId}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteQueryConfig(@PathVariable("queryId") String queryId){
        return Exec.exec("delete query-config", () -> this.queryConfigService.deleteQueryConfig(queryId) );
    }

}
