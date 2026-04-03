package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.mybatis.dto.GlAccountInterfaceExt;
import it.mapsgroup.gzoom.service.GlAccountInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class GlAccountInterfaceController {

    private final GlAccountInterfaceService glAccountInterfaceService;

    @Autowired
    public GlAccountInterfaceController(GlAccountInterfaceService glAccountInterfaceService) {this.glAccountInterfaceService = glAccountInterfaceService;}

    @RequestMapping(value = "glaccountinterfaceexts", method = RequestMethod.POST)
    @ResponseBody
    public String createGlAccountInterfaceExt(@RequestBody GlAccountInterfaceExt req) {
        return Exec.exec("create glaccountinterfaceext", () -> glAccountInterfaceService.createExt(req));
    }
}
