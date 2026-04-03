package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.model.Result;
import it.mapsgroup.gzoom.mybatis.dto.AcctgTransInterface;
import it.mapsgroup.gzoom.mybatis.dto.AcctgTransInterfaceExt;
import it.mapsgroup.gzoom.service.AcctgTransInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "", produces = { MediaType.APPLICATION_JSON_VALUE })
public class AcctgTransInterfaceController {

    private final AcctgTransInterfaceService acctgTransInterfaceService;

    @Autowired
    public AcctgTransInterfaceController(AcctgTransInterfaceService acctgTransInterfaceService){this.acctgTransInterfaceService = acctgTransInterfaceService;}

    @RequestMapping(value = "acctgtransinterfaces", method = RequestMethod.POST)
    @ResponseBody
    public String createAcctgTransInterface(@RequestBody AcctgTransInterface req) {
        return Exec.exec("create acctgtransinterfaces", () -> acctgTransInterfaceService.createAcctgTransInterface(req));
    }

    @RequestMapping(value = "acctgtransinterfaces", method = RequestMethod.GET)
    @ResponseBody
    public Result<AcctgTransInterface> getAllAcctgTransInterfaces() {
        return Exec.exec("get all acctgtransinterfaces", () -> acctgTransInterfaceService.getAcctgTransInterfaces());
    }

    @RequestMapping(value = "acctgtransinterfaceexts", method = RequestMethod.POST)
    @ResponseBody
    public String createAcctgTransInterfaceExt(@RequestBody AcctgTransInterfaceExt req) {
        return Exec.exec("create acctgtransinterfaceext", () -> acctgTransInterfaceService.createAcctgTransInterfaceExt(req));
    }
}
