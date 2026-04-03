package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.StandardImportFieldConfig;
import it.mapsgroup.gzoom.mybatis.dto.StandardImportFieldConfigEx;
import it.mapsgroup.gzoom.service.StandardImportFieldConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "standard-import-field-config", produces = { MediaType.APPLICATION_JSON_VALUE })
public class StandardImportFieldConfigController {
    private final StandardImportFieldConfigService standardImportFieldConfigService;

    @Autowired
    public StandardImportFieldConfigController(StandardImportFieldConfigService standardImportFieldConfigService) {
        this.standardImportFieldConfigService = standardImportFieldConfigService;
    }

    @GetMapping(value = "/standardImportFieldConfig")
    @ResponseBody
    public Result<StandardImportFieldConfig> getStandardImportFieldConfig(){
        return Exec.exec("get data-source", this.standardImportFieldConfigService::getStandardImportFieldConfig);
    }

    @GetMapping(value = "/standardImportFieldConfigEx/{dataSourceId}")
    @ResponseBody
    public Result<StandardImportFieldConfigEx> getStandardImportFieldConfigEx(@PathVariable(value = "dataSourceId") String dataSourceId){
        return Exec.exec("get data-source-ex", () -> this.standardImportFieldConfigService.getStandardImportFieldConfigEx(dataSourceId));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public StandardImportFieldConfig createStandardImportFieldConfig(@RequestBody StandardImportFieldConfig standardImportFieldConfig){
        return Exec.exec("create standard-import-field-config", () -> this.standardImportFieldConfigService.createStandardImportFieldConfig(standardImportFieldConfig) );
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    @ResponseBody
    public Boolean updateStandardImportFieldConfig(@RequestBody StandardImportFieldConfig standardImportFieldConfig) {
        return Exec.exec("update standard-import-field-config", () -> this.standardImportFieldConfigService.updateStandardImportFieldConfig(standardImportFieldConfig));
    }

    @DeleteMapping(value = "/{dataSourceId}/{standardInterface}/{internalFieldName}/{interfaceSeq}")
    @ResponseBody
    public boolean deleteStandardImportFieldConfig(@PathVariable(value = "dataSourceId") String dataSourceId, @PathVariable(value = "standardInterface") String standardInterface, @PathVariable(value = "internalFieldName") String internalFieldName, @PathVariable(value = "interfaceSeq") BigDecimal interfaceSeq){
        return Exec.exec("delete standard-import-field-config", () -> this.standardImportFieldConfigService.deleteStandardImportFieldConfig(dataSourceId, standardInterface, internalFieldName, interfaceSeq) );
    }
}
