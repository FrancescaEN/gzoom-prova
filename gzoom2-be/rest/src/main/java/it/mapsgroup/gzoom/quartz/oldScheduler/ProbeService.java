package it.mapsgroup.gzoom.quartz.oldScheduler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.persistence.common.dto.enumeration.ReportActivityStatus;
import it.mapsgroup.gzoom.report.report.dto.ReportStatus;
import it.mapsgroup.gzoom.report.service.ReportCallbackService;
import it.mapsgroup.gzoom.report.service.ReportCallbackType;
import it.mapsgroup.gzoom.service.GzoomReportClientConfig;

import it.mapsgroup.gzoom.service.Validators;
import it.mapsgroup.gzoom.service.report.ReportClientService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
@Service
public class ProbeService {
    private static final Logger LOG = getLogger(ProbeService.class);

    private final ReportClientService reportClientService;
    private final ProbeSchedulerService schedulerService;
    private final ReportCallbackService reportCallbackService;
    private final ObjectMapper objectMapper;
    private final GzoomReportClientConfig config;

    @Autowired
    public ProbeService(ReportClientService reportClientService,
                        ProbeSchedulerService schedulerService,
                        ReportCallbackService reportCallbackService,
                        ObjectMapper objectMapper, GzoomReportClientConfig config) {
        this.reportClientService = reportClientService;
        this.schedulerService = schedulerService;
        this.reportCallbackService = reportCallbackService;
        this.objectMapper = objectMapper;
        this.config = config;
    }

    public void probeReport(String id, String callbackType, String jsonParams) {
        ResponseEntity<ReportStatus> status = null;
        try {
            status = reportClientService.getStatus(id);
        } catch (Exception e) {
            LOG.error("Cannot probe report[{}]", id);
            LOG.debug("Rescheduling report probe [{}]", id);
            schedulerService.updateReportProbe(id, false);
            return;
        }
        Map<String, Object> params = Collections.emptyMap();
        if (StringUtils.isNotEmpty(callbackType)) {
            try {
                JsonTypeMap<String, Object> jsonMap =
                        objectMapper.readValue(jsonParams, new TypeReference<JsonTypeMap<String, Object>>() {
                        });
                params = jsonMap.get();
            } catch (IOException e) {
                LOG.error("Cannot parse callback data. ReportActivity[{}]", id);

            }
        }
        ReportCallbackType callback = null;
        try {
            if (StringUtils.isNotEmpty(callbackType))
                callback = Validators.assertIsEnum(ReportCallbackType.class, callbackType, "Invalid callback");
        } catch (Exception e) {
            LOG.error("Invalid callback. ReportActivity[{}]", id);
        }

        LOG.info("Report status: {}", status.getBody().getActivityStatus());
        LOG.info("******************* {} - {} ******************* ", status.getBody().getActivityStatus(), id);

        if (StringUtils.equals(ReportActivityStatus.DONE.name(), status.getBody().getActivityStatus())) {
            LOG.debug("UnScheduling report probe [{}]", id);
            schedulerService.unScheduleReportProbe(id);
            if (callback != null) {
                LOG.debug("Running callback [{}]", callback);
                reportCallbackService.reportDone(id, callback, params);
            }
        }
        if (
                StringUtils.equals(ReportActivityStatus.QUEUED.name(), status.getBody().getActivityStatus()) ||
                        StringUtils.equals(ReportActivityStatus.RUNNING.name(), status.getBody().getActivityStatus())
        ) {
            LOG.debug("Rescheduling report probe [{}]", id);
            schedulerService.updateReportProbe(id, false);
        }
        if (
                StringUtils.equals(ReportActivityStatus.FAILED.name(), status.getBody().getActivityStatus()) ||
                        StringUtils.equals(ReportActivityStatus.CANCELLED.name(), status.getBody().getActivityStatus())
        ) {
            LOG.debug("UnScheduling report probe [{}]", id);
            schedulerService.unScheduleReportProbe(id);
        }


    }
}
