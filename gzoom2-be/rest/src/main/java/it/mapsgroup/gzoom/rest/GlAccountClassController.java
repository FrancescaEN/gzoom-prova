package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountClass;
import it.mapsgroup.gzoom.mybatis.dto.GlResourceType;
import it.mapsgroup.gzoom.service.GlAccountClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "gl-account-class", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlAccountClassController {
    private final GlAccountClassService glAccountClassService;

    @Autowired
    public GlAccountClassController(GlAccountClassService glAccountClassService) {
        this.glAccountClassService = glAccountClassService;
    }

    @GetMapping("{accountTypeEnumId}")
    @ResponseBody
    public Result<GlAccountClass> getByAccountTypeEnumId(@PathVariable String accountTypeEnumId){
        return Exec.exec("get gl-account-class by account-type-enum-id", () -> this.glAccountClassService.getByAccountTypeEnumId(accountTypeEnumId));
    }
}
