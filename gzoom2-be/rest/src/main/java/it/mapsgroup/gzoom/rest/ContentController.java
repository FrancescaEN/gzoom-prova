package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.Content;
import it.mapsgroup.gzoom.mybatis.dto.ContentExDataResource;
import it.mapsgroup.gzoom.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "content", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ContentController {

    private final ContentService contentService;

    @Autowired
    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping(value = "/{contentTypeId}")
    @ResponseBody
    public Result<ContentExDataResource> getContentListByContentTypeId(@PathVariable(value = "contentTypeId") String contentTypeId){
        return Exec.exec("get content ex data resource by contentTypeId", () -> this.contentService.getContentListByContentTypeId(contentTypeId));
    }

    @GetMapping(value = "/content-type-id/{contentTypeId}")
    @ResponseBody
    public Result<Content> findByContentTypeId(@PathVariable String contentTypeId){
        return Exec.exec("get content by contentTypeId", () -> this.contentService.findByContentTypeId(contentTypeId));
    }
}
