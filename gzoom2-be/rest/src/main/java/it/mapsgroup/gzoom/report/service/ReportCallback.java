package it.mapsgroup.gzoom.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Andrea Fossi.
 */
public abstract class ReportCallback implements InitializingBean, DisposableBean  {
    private final ReportCallbackManager callBackManager;

    @Autowired
    public ReportCallback(ReportCallbackManager callBackManager) {
        this.callBackManager = callBackManager;
    }

    //@PostConstruct
    @SuppressWarnings("unchecked")
    private void init() {
        this.callBackManager.register(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    @Override
    public void destroy() throws Exception {
    }

    public abstract ReportCallbackType getType();

    public abstract void run(String reportActivityId, Map<String, Object> params);
}
