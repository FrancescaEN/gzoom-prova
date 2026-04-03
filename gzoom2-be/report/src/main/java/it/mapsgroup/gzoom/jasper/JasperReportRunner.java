package it.mapsgroup.gzoom.jasper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.export.*;
import net.sf.jasperreports.repo.FileRepositoryPersistenceServiceFactory;
import net.sf.jasperreports.repo.FileRepositoryService;
import net.sf.jasperreports.repo.PersistenceServiceFactory;
import net.sf.jasperreports.repo.RepositoryService;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Collections;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Qualifier("jasper")
public class JasperReportRunner implements ReportRunner {
	public static final String JASPER_PARAMETERS = "jasperParameters";
	private Logger logger = getLogger(JasperReportRunner.class);
	private String reportTempDirectory;
	private final JasperConfig config;
	private String developerBirtPath;
//	private Connection conn = null;
    private DataSource jasperDataSource;

	@Autowired
	public JasperReportRunner(JasperConfig config, @Qualifier("jasperDataSource") DataSource jasperDataSource) {
		this.config = config;
		this.jasperDataSource = jasperDataSource;
	}


	@PostConstruct
	@SuppressWarnings("unchecked")
	public void startUp() {
		if (ObjectUtils.isEmpty(config.getJasperReportInputDir())) {
			throw new RuntimeException("Cannot start application since jasper report input directory was not specified.");
		}
//		try {
			logger.info("JASPER LOG DIRECTORY SET TO : {}", config.getJasperLoggingDirectory());
			logger.info("JASPER LOGGING LEVEL SET TO {}", config.getJasperLoggingLevel());
//			// data source
//			if (!StringUtils.isEmpty(config.getOdaUrl())) {
//				logger.info("Setting data source url: {}", config.getOdaUrl());
//				logger.info("Setting data source driver: {}", config.getOdaDriverClass());
//				conn = getConnection(config.getOdaUrl(),config.getOdaUser(),config.getOdaPassword());
//			}
//		} catch (Exception e) {
//			logger.error("Jasper Startup Error: {}", e.getMessage());
//		}
		reportTempDirectory = config.getJasperTempFileOutputDir();
		developerBirtPath = config.getDeveloperBirtPath();
	}

	@PreDestroy
	public void shutdown() {
		//jasperReportEngine.destroy();
		RegistryProviderFactory.releaseDefault();
		//Platform.shutdown();
	}

	public String getReportFromFileSystem(String parentTypeId, String resourceName) throws RuntimeException {
		String reportDirectory = config.getJasperReportInputDir();
		//final String jasperExtension = ".jrxml";
		final String jasperExtension = ".jasper";
		Path jasperReport;
		if (developerBirtPath!=null && !developerBirtPath.equals(""))
			jasperReport = Paths.get( reportDirectory + File.separator + "project" + File.separator + developerBirtPath + File.separator + resourceName + File.separator + resourceName + jasperExtension);
		else
			jasperReport = Paths.get( reportDirectory + File.separator + "custom" + File.separator + resourceName + File.separator + resourceName + jasperExtension);
		if (!Files.isReadable(jasperReport)) {
			logger.info("Cannot loading jrxml project/custom: "+jasperReport);
			jasperReport = Paths.get(reportDirectory + File.separator + parentTypeId + File.separator + resourceName + File.separator + resourceName + jasperExtension);
			if (!Files.isReadable(jasperReport)) {
				logger.info("Cannot loading jrxml 2nd step: "+jasperReport);
				jasperReport = Paths.get(reportDirectory + File.separator + resourceName + File.separator + resourceName + jasperExtension);
				if (!Files.isReadable(jasperReport)) {
					logger.info("Cannot loading jrxml 3th step: "+jasperReport);
					throw new RuntimeException("Report " + resourceName + " either did not exist or was not writable.");
				}
			}
		}
		return jasperReport.toString();
	}


	@Override
	@SuppressWarnings("unchecked")
	public ReportHandler runReport(Report jasperReport) {
		String reportSrcFile = getReportFromFileSystem(jasperReport.getType(), jasperReport.getName());
		/*
		JasperReport jasperReportTest = null;
		try {
			jasperReportTest = JasperCompileManager.compileReport(reportSrcFile);
		} catch (Exception e) {
		e.printStackTrace();
		}
		*/
		File reportFile = null;
		Connection conn = null;
		try {
			Map<String, Object> reportParameters = jasperReport.getParameters();
			SimpleJasperReportsContext context = new SimpleJasperReportsContext();
			FileRepositoryService fileRepository = new FileRepositoryService(context, reportSrcFile.substring(0,reportSrcFile.lastIndexOf(File.separator)), true);
			context.setExtensions(RepositoryService.class, Collections.singletonList(fileRepository));
			context.setExtensions(PersistenceServiceFactory.class, Collections.singletonList(FileRepositoryPersistenceServiceFactory.getInstance()));
			reportParameters.put(JRParameter.REPORT_LOCALE, jasperReport.getReportLocale());
			conn = DataSourceUtils.getConnection(jasperDataSource);
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportSrcFile, reportParameters, conn);
			//jasperPrint.setOrientation(OrientationEnum.LANDSCAPE);
			reportFile = new File(reportTempDirectory + File.separator + "jasper_report_temp_file_" + jasperReport.getTaskId() + ".pdf");
			reportFile.deleteOnExit();
			JRPdfExporter exporter = new JRPdfExporter();
			ExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
			exporter.setExporterInput(exporterInput);
			OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(reportFile);
			exporter.setExporterOutput(exporterOutput);
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		} catch (Exception e) {
			e.printStackTrace(); // Per essere sicuri che logghi almeno su default output
			logUnexpectedException(jasperReport, reportSrcFile, e);
		} finally {
			DataSourceUtils.releaseConnection(conn, jasperDataSource);
		}
		return new ReportHandler(reportFile);
	}

	private void logUnexpectedException(Report jasperReport, String reportSrcFile, Exception e) {
		try {
			logger.error("Eccezione inattesa nell'esecuzione del report _" + jasperReport.getName() + "_:"
					+ "\r\n\tNome = " + jasperReport.getName()
					+ "\r\n\tPath completo = " + reportSrcFile
					+ "\r\n\tTipo = " + jasperReport.getType()
					+ "\r\n\tNumero parametri = " + ( jasperReport.getParameters() != null ? jasperReport.getParameters().size() : "(nessuno)" )
					// Info eccezione
					+ "\r\n"
					+ "\r\n\tEccezione: "
					+ "\r\n\t\t" + e.getClass().getName() + " - " + e.getMessage() // tipo / messaggio (senza stack)
			, e); // Stampo anche il full stack
		} catch (Exception inner) {
			// Se la stampa dell'eccezione dà un'eccezione
			logger.error("Eccezione inattesa nell'esecuzione del report: " + e.getClass().getName()  + " - " + e.getMessage()
					+ " [" + inner.getClass().getName()  + " - " + inner.getMessage() + "]");
		}
	}


//	private static Connection getConnection(String url, String user, String password) throws SQLException {
//		return DriverManager.getConnection(url, user, password);
//	}
}
