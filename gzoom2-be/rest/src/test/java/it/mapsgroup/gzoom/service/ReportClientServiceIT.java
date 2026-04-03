package it.mapsgroup.gzoom.service;

import it.mapsgroup.gzoom.report.report.dto.ReportStatus;
import it.mapsgroup.gzoom.service.report.ReportClientService;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
public class ReportClientServiceIT {
    private static final Logger LOG = getLogger(ReportClientServiceIT.class);

    @Autowired
    GzoomReportClientConfig config;
    
    @Test
    @Ignore
    public void sampleTest() throws MalformedURLException {
        ReportClientService client = new ReportClientService(new RestTemplate(), config);
        String id = client.createReport();
        ResponseEntity<ReportStatus> status = client.getStatus(id);

        LOG.info(status.getBody().toString());
    }
}
