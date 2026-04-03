package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.service.InterfacciamentoDatiService;
import it.mapsgroup.gzoom.service.QueryExecutorService;
import jakarta.servlet.http.HttpServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class InterfacciamentoDatiController extends HttpServlet {

    private final it.mapsgroup.gzoom.service.InterfacciamentoDatiService InterfacciamentoDatiService;
    private final QueryExecutorService queryExecutorService;

    @Autowired
    public InterfacciamentoDatiController(InterfacciamentoDatiService InterfacciamentoDatiService, QueryExecutorService queryExecutorService) {
        this.InterfacciamentoDatiService = InterfacciamentoDatiService;
        this.queryExecutorService = queryExecutorService;
    }


    @RequestMapping(value = "interfacciamentoDati", method = RequestMethod.POST)
    @ResponseBody
    protected boolean doPost(@RequestBody MultipartFile file) throws IOException, SQLException {
        System.out.print("UPLOAD FILE\n");
        return InterfacciamentoDatiService.ExecuteInsertRowFile(file, file.getOriginalFilename());
    }
}
