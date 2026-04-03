package it.mapsgroup.gzoom.report.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
@Service
public class ReportCallbackManager<T extends ReportCallback> {
    private static final Logger LOG = getLogger(ReportCallbackManager.class);

    private final Map<ReportCallbackType, T> callbacks;

    public ReportCallbackManager() {
        callbacks = new HashMap<>();
    }

    public void register(T callback) {
        LOG.debug("Registering class {} for {}", callback.getClass(), callback.getType());
        this.callbacks.put(callback.getType(), callback);
    }

    public T get(ReportCallbackType callbackType) {
        return callbacks.get(callbackType);
    }
}
