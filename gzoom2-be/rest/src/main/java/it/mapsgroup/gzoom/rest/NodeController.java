package it.mapsgroup.gzoom.rest;

import it.mapsgroup.gzoom.common.Exec;
import it.mapsgroup.gzoom.ofbiz.client.OfBizClientConfig;
import it.mapsgroup.gzoom.mybatis.dto.PartyNoteEx;
import it.mapsgroup.gzoom.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Fabio G. Strozzi
 */
@RestController
@RequestMapping(value = "node", produces = {MediaType.APPLICATION_JSON_VALUE})
public class NodeController {

    public static final String APPLICATION_TITLE = "APPLICATION_TITLE";

    private final NodeService nodeService;
    private final OfBizClientConfig ofBizClientConfig;

    @Autowired
    public NodeController(NodeService nodeService, OfBizClientConfig ofBizClientConfig) {
        this.nodeService = nodeService;
        this.ofBizClientConfig = ofBizClientConfig;
    }

    @RequestMapping(value = "/configuration/{partyId}", method = RequestMethod.GET)
    @ResponseBody
    public PartyNoteEx getApplicationTitle(@PathVariable(value = "partyId") String partyId) {
        return Exec.exec("configuration get", () -> nodeService.getNodeConfiguration(partyId, APPLICATION_TITLE));
    }

    @RequestMapping(value = "/configuration/xmlrcpurl", method = RequestMethod.GET)
    @ResponseBody
    public String getServerXmlRpcUrl()
    {
        return Exec.exec("get server xmlrcpurl", () -> ofBizClientConfig.getServerXmlRpcUrl().toString());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/logo/{partyId}/{partyContentTypeId}")
    @ResponseBody
    public String stream(@PathVariable(value = "partyId") String partyId, @PathVariable(value = "partyContentTypeId") String partyContentTypeId, HttpServletRequest req, HttpServletResponse response) {
        return Exec.exec("logo stream", () -> nodeService.stream(partyId, partyContentTypeId, req, response));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/version/legacy")
    @ResponseBody
    public String versionLegacy() {
        return Exec.exec("legacy versions", () -> nodeService.versionLegacy());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/version/rest")
    @ResponseBody
    public String versionREST() {
        return Exec.exec("rest versions", () -> nodeService.versionREST());
    }
}
