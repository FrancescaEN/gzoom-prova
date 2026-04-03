package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.PortalPage;
import it.mapsgroup.gzoom.service.PortalPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "portal-page", produces = { MediaType.APPLICATION_JSON_VALUE })
public class PortalPageController {
    private final PortalPageService portalPageService;

    @Autowired
    public PortalPageController(PortalPageService portalPageService) {
        this.portalPageService = portalPageService;
    }

    @GetMapping("/parent-portal-page-id/{parentPortalPageId}")
    @ResponseBody
    public Result<PortalPage> findByParentPortalPageId(@PathVariable String parentPortalPageId){
        return Exec.exec("get portal page list by parentPortalPageId", () -> this.portalPageService.findByParentPortalPageId(parentPortalPageId));
    }
}
