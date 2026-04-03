package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.CustomMethodMatrix;
import it.mapsgroup.gzoom.service.CustomMethodMatrixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "custom-method-matrix", produces = { MediaType.APPLICATION_JSON_VALUE })
public class CustomMethodMatrixController {


    private final CustomMethodMatrixService customMethodMatrixService;

    @Autowired
    public CustomMethodMatrixController(CustomMethodMatrixService customMethodMatrixService) {
        this.customMethodMatrixService = customMethodMatrixService;
    }

    @GetMapping
    @ResponseBody
    public Result<CustomMethodMatrix> getCustomMethodMatrixList(@RequestParam String customMethodId) {
        return Exec.exec("custom-method-matrix-list/ get", () -> customMethodMatrixService.getCustomMethodMatrixList(customMethodId));
    }

    @PostMapping
    @ResponseBody
    public String createCustomMethodMatrix(@RequestBody CustomMethodMatrix req){
        return Exec.exec("create custom-method-matrix", () -> this.customMethodMatrixService.createCustomMethodMatrix(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateCustomMethodMatrix(@RequestBody CustomMethodMatrix req){
        return Exec.exec("update custom-method-matrix", () -> this.customMethodMatrixService.updateCustomMethodMatrix(req) );
    }

    @DeleteMapping(value = "/{customMethodMatrixId}")
    @ResponseBody
    public boolean deleteCustomMethodMatrix(@PathVariable(value = "customMethodMatrixId") String customMethodMatrixId){
        return Exec.exec("delete custom-method-matrix", () -> this.customMethodMatrixService.deleteCustomMethodMatrix(customMethodMatrixId) );
    }
}
