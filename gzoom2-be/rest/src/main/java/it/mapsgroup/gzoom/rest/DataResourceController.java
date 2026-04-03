package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.mybatis.dto.WorkEffortContentEx;
import it.mapsgroup.gzoom.service.DataResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "data-resource", produces = { MediaType.APPLICATION_JSON_VALUE })
public class DataResourceController {
    private final DataResourceService dataResourceService;

    @Autowired
    public DataResourceController(DataResourceService dataResourceService) {
        this.dataResourceService = dataResourceService;
    }

    @RequestMapping(value = "/uploadFile/{dataResourceId}", method = RequestMethod.POST)
    @ResponseBody
    public WorkEffortContentEx uploadFile(@PathVariable("dataResourceId") String dataResourceId, @RequestBody MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        return dataResourceService.uploadFile(file, file.getOriginalFilename(), dataResourceId);
    }
}
