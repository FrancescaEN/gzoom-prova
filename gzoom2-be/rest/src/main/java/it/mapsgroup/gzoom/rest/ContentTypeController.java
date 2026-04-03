package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.ContentType;
import it.mapsgroup.gzoom.service.ContentTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "content-type", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ContentTypeController {

    private final ContentTypeService contentTypeService;

    @Autowired
    public ContentTypeController(ContentTypeService contentTypeService) {
        this.contentTypeService = contentTypeService;
    }

    @GetMapping(value = "/{parentTypeId}")
    @ResponseBody
    public Result<ContentType> getContentTypeWithParentTypeId(@PathVariable(value = "parentTypeId") String parentTypeId){
        return Exec.exec("get content-type width parent type", () -> this.contentTypeService.getContentTypeWithParentTypeId(parentTypeId) );
    }
}
