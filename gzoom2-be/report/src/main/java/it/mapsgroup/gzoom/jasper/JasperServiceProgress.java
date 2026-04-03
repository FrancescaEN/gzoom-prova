package it.mapsgroup.gzoom.jasper;

import it.mapsgroup.gzoom.commons.collect.Tuple2;
import it.mapsgroup.gzoom.dto.ReportStatus;
import org.slf4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.slf4j.LoggerFactory.getLogger;


public class JasperServiceProgress  {
    private static final Logger LOG = getLogger(JasperServiceProgress.class);
    private final AtomicInteger queryCount;
    private final AtomicInteger pageCount;
    private final AtomicReference<Tuple2<Boolean, String>> cancelled;


    public JasperServiceProgress()  {
        queryCount = new AtomicInteger(0);
        pageCount = new AtomicInteger(0);
        cancelled = new AtomicReference<Tuple2<Boolean, String>>(new Tuple2<>(false, null));
    }

    public void onProgress(int type, int value) {
    }

    public void cancel(String reason) {
        cancelled.set(new Tuple2<>(true, reason));
    }

    public ReportStatus getStatus() {
        return null;
    }
}
