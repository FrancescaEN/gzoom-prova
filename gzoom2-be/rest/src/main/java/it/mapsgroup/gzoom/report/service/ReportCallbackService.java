package it.mapsgroup.gzoom.report.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
@Service
public class ReportCallbackService {
    private static final Logger LOG = getLogger(ReportCallbackService.class);

    private final ReportCallbackManager callBackManager;


    @Autowired
    public ReportCallbackService(ReportCallbackManager callBackManager) {
        this.callBackManager = callBackManager;
    }

    public void reportDone(String reportActivityId, ReportCallbackType callbackType, Map<String, Object> params) {
        if (callbackType != null) {
            ReportCallback reportCallback = this.callBackManager.get(callbackType);
            if (reportCallback == null) {
                LOG.error("Callback not found. ReportActivity[{}], Callback[{}]", reportActivityId, callbackType);
                return;
            } else {
                reportCallback.run(reportActivityId,params);
            }
        }
    }

}
