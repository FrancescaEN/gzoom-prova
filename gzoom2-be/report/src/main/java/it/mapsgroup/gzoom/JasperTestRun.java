package it.mapsgroup.gzoom;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.repo.FileRepositoryPersistenceServiceFactory;
import net.sf.jasperreports.repo.FileRepositoryService;
import net.sf.jasperreports.repo.PersistenceServiceFactory;
import net.sf.jasperreports.repo.RepositoryService;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class JasperTestRun {

    public static void main(String[] args) {

        try {
            //String reportSrcFile = "C:/Users/antcal/JaspersoftWorkspace/MyReports/test.jrxml";
            String reportSrcFile = "C:/Users/gafr/JaspersoftWorkspace/MyReports/Cherry.jrxml";
            // First, compile jrxml file.
            JasperReport jasperReport = JasperCompileManager.compileReport(reportSrcFile);

            Connection conn = null;
            try {
                conn = getConnection();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            // Parameters for report
            Map<String, Object> parameters = new HashMap<>();
            SimpleJasperReportsContext context = new SimpleJasperReportsContext();
            FileRepositoryService fileRepository = new FileRepositoryService(context, "C:/Users/gafr/JaspersoftWorkspace/MyReports", true);
            context.setExtensions(RepositoryService.class, Collections.singletonList(fileRepository));
            context.setExtensions(PersistenceServiceFactory.class, Collections.singletonList(FileRepositoryPersistenceServiceFactory.getInstance()));

            JasperPrint jasperPrint = JasperFillManager.getInstance(context).fill(jasperReport, parameters, conn);

            File outDir = new File("C:/temp");
            outDir.mkdirs();

            JRPdfExporter exporter = new JRPdfExporter();

            ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);

            exporter.setExporterInput(exporterInput);

            OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput("C:/temp/Cherry.pdf");

            exporter.setExporterOutput(exporterOutput);
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

            exporter.setConfiguration(configuration);
            exporter.exportReport();

            System.out.println("DONE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS = (PROTOCOL = TCP)(HOST = localhost)(PORT = 1521))(CONNECT_DATA =(SERVER = DEDICATED)(SERVICE_NAME = orcl12c)))", "GZ_BOLZANOWIN", "Ab123456");
    }
}