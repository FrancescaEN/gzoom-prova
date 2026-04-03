package it.mapsgroup.gzoom.report.service;

import it.mapsgroup.gzoom.service.GzoomReportClientConfig;
import it.mapsgroup.gzoom.service.report.ReportClientService;
import it.mapsgroup.gzoom.mybatis.dto.ReportActivity;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
@Component
public class TestCallback extends ReportCallback {
    private static final Logger LOG = getLogger(TestCallback.class);

    private final ReportClientService client;
    private final GzoomReportClientConfig config;

    @Autowired
    public TestCallback(ReportCallbackManager callBackManager, ReportClientService client, GzoomReportClientConfig config) {
        super(callBackManager);
        this.client = client;
        this.config = config;
    }

    @Override
    public ReportCallbackType getType() {
        return ReportCallbackType.TEST;
    }

    @Transactional
    @Override
    public void run(String reportActivityId, Map<String, Object> params) {
        LOG.info("TEST - params[description]={}", params.get("description"));
        ResponseEntity<ReportActivity> reportActivity = client.getReportActivity(reportActivityId);
        ReportActivity report = reportActivity.getBody();
        LOG.info("Report name {}", report.getReportName());

    }
}
