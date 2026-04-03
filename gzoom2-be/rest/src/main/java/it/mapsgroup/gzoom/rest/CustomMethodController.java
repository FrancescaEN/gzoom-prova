package it.mapsgroup.gzoom.rest;


import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.CustomMethod;
import it.mapsgroup.gzoom.service.CustomMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author Leonardo Minaudo.
 */
@RestController
@RequestMapping(value = "custom-method", produces = { MediaType.APPLICATION_JSON_VALUE })
public class CustomMethodController {
    private final CustomMethodService customMethodService;

    @Autowired
    public CustomMethodController(CustomMethodService customMethodService) {
        this.customMethodService = customMethodService;
    }

    @GetMapping
    @ResponseBody
    public Result<CustomMethod> getCustomMethodList(){
        return Exec.exec("get custom-method-list", this.customMethodService::getCustomMethodList);
    }

    @PostMapping
    @ResponseBody
    public boolean createCustomMethod(@RequestBody CustomMethod req){
        return Exec.exec("create custom-method", () -> this.customMethodService.createCustomMethod(req) );
    }

    @PutMapping
    @ResponseBody
    public boolean updateCustomMethod(@RequestBody CustomMethod req){
        return Exec.exec("update custom-method", () -> this.customMethodService.updateCustomMethod(req) );
    }

    @PostMapping(value = "delete")
    @ResponseBody
    public boolean deleteCustomMethod(@RequestBody String id){
        return Exec.exec("delete custom-method", () -> this.customMethodService.deleteCustomMethod(id) );
    }
}
