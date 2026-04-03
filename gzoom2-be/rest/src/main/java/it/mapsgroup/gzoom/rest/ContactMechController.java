package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.mybatis.dto.ContactMech;
import it.mapsgroup.gzoom.service.ContactMechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "contactMech", produces = { MediaType.APPLICATION_JSON_VALUE })
public class ContactMechController {

    private final ContactMechService contactMechService;

    @Autowired
    public ContactMechController(ContactMechService contactMechService) {
        this.contactMechService = contactMechService;
    }

    @RequestMapping(value = "/infoString", method = RequestMethod.GET)
    @ResponseBody
    public ContactMech getInfoString() {
        return Exec.exec("contactMech getInfoString get", contactMechService::getInfoString);
    }
}
