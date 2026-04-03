package it.mapsgroup.gzoom.birt;

import org.slf4j.Logger;

import java.io.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
public class ReportHandler {
    private static final Logger LOG = getLogger(ReportHandler.class);

    private final File reportContent;

    public ReportHandler(File reportContent) {
        this.reportContent = reportContent;
    }

    public InputStream getReportContent() {
        if (reportContent == null) return new ByteArrayInputStream(new byte[0]);
        try {
            return new FileInputStream(reportContent);
        } catch (FileNotFoundException e) {
            LOG.error("Cannot read file", e);
            throw new RuntimeException(e);//fixme
        }
    }

    public void close() {
        try {
            reportContent.delete();
        } catch (Exception e) {
            LOG.error("Cannot delete file", e);
            throw new RuntimeException(e);//fixme
        }
    }
}
