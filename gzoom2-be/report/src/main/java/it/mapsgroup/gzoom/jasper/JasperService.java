package it.mapsgroup.gzoom.jasper;


import it.mapsgroup.gzoom.util.pdf.ConvertPDFtoA3;
import it.mapsgroup.gzoom.util.pdf.PDFA3Components;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;


@Service
public class JasperService {
    private static final Logger LOG = getLogger(JasperService.class);

    //private final BIRTReportRunner birtReportRunner;
    private final JasperConfig config;
    private final ReportRunner reportRunner;


    @Autowired
    public JasperService(JasperConfig config, ReportRunner reportRunner) {
        //this.birtReportRunner = birtReportRunner;
        this.config = config;
        this.reportRunner = reportRunner;
    }


    /**
     * Method that create a build report
     *
     * @param taskId
     * @param resourceName
     * @param parentTypeId
     * @param reportParams
     * @param locale
     */
    public it.mapsgroup.gzoom.jasper.Report build(String taskId, String resourceName, String parentTypeId, Map<String, Object> reportParams, Locale locale) {
        it.mapsgroup.gzoom.jasper.Report report = new JasperReport(taskId, resourceName, parentTypeId, reportParams, locale);
        return report;
    }

    /**
     * @param outputFileName
     * @param report
     * @return absolute path of report
     */
    public String run(String outputFileName, Report report) {
        ReportHandler reportHandler = reportRunner.runReport(report);
        String outputFormat = (String) report.getParameters().get("outputFormat");
        File outputPath = new File(config.getJasperReportOutputDir(), outputFileName + "." + outputFormat);
        try {
            FileOutputStream out = new FileOutputStream(outputPath);
            IOUtil.copyCompletely(reportHandler.getReportContent(), out);
            // TODO (anche negli altri casi simili): se reportHandler.reportContent è null, evitare che vada in eccezione generica
            reportHandler.close();
            //GN-5131
            //Convert BIRT generated pdf to pdf/a and overwrite the related output file
            PDFA3Components pdfa3Components = new PDFA3Components(outputPath.getPath(),outputPath.getPath());
            ConvertPDFtoA3 converter = new ConvertPDFtoA3();
            converter.Convert(pdfa3Components);
            return outputPath.getPath();
        } catch (IOException e) {
            LOG.error("Cannot save report", e);
            throw new RuntimeException(e);//fixme manage exception
        } catch (Exception e) {
            LOG.error("Cannot save report", e);
            throw new RuntimeException(e);
        }
    }

}
