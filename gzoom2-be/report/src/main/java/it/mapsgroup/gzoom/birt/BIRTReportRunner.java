package it.mapsgroup.gzoom.birt;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.*;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.internal.registry.RegistryProviderFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Service
@Qualifier("birt")
public class BIRTReportRunner implements ReportRunner {
	public static final String BIRT_PARAMETERS = "birtParameters";
	private Logger logger = getLogger(BIRTReportRunner.class);
	private String reportTempDirectory;
	private IReportEngine birtReportEngine;
	private final BirtConfig config;
	private String developerBirtPath;

	@Autowired
	public BIRTReportRunner(BirtConfig config) {
		this.config = config;
	}

	/**
	 * Starts up and configures the BIRT Report Engine
	 */
	@PostConstruct
	@SuppressWarnings("unchecked")
	public void startUp() throws BirtException {
		if (!StringUtils.hasLength(config.getBirtReportInputDir()))
			throw new RuntimeException("Cannot start application since birt report input directory was not specified.");
		try {
			// String birtLoggingDirectory =
			// env.getProperty("birt.logging.directory") == null ?
			// DEFAULT_LOGGING_DIRECTORY :
			// env.getProperty("birt.logging.directory");
			// Level birtLoggingLevel = env.getProperty("birt.logging.level") ==
			// null ? Level.SEVERE :
			// Level.parse(env.getProperty("birt.logging.level"));
			EngineConfig engineConfig = new EngineConfig();

			logger.info("BIRT LOG DIRECTORY SET TO : {}", config.getBirtLoggingDirectory());
			logger.info("BIRT LOGGING LEVEL SET TO {}", config.getBirtLoggingLevel());
			engineConfig.setLogConfig(config.getBirtLoggingDirectory(), config.getBirtLoggingLevel());
			engineConfig.setLogger(java.util.logging.Logger.getLogger(BIRTReportRunner.class.getName()));
			engineConfig.setResourcePath(config.getBirtReportInputDir());
			engineConfig.setBIRTHome(config.getBirtReportInputDir());
			// Required due to a bug in BIRT that occurs in calling Startup
			// after the Platform has already been started up
			RegistryProviderFactory.releaseDefault();
			Platform.startup(engineConfig);
			// data source
			if (StringUtils.hasLength(config.getOdaUrl())) {
				logger.info("Setting data source url: {}", config.getOdaUrl());
				logger.info("Setting data source driver: {}", config.getOdaDriverClass());
				engineConfig.getAppContext().put("odaURL", config.getOdaUrl());
				engineConfig.getAppContext().put("odaDriverClass", config.getOdaDriverClass());
				engineConfig.getAppContext().put("odaUser", config.getOdaUser());
				engineConfig.getAppContext().put("odaPassword", config.getOdaPassword());
				engineConfig.getAppContext().put("odaIsolationMode",config.getOdaIsolationMode());
				engineConfig.getAppContext().put("odaDialect", config.getOdaDialect());
			}
			IReportEngineFactory reportEngineFactory = (IReportEngineFactory) Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
			birtReportEngine = reportEngineFactory.createReportEngine(engineConfig);

		} catch (BirtException e) {
			// TODO add logging aspect and find out how to log a platform
			// startup problem from this catch block, if possible, using the
			// aspect.
			// Possibly rethrow the exception here and catch it in the aspect.
			logger.error("Birt Startup Error: {}", e.getMessage());
		}
		reportTempDirectory = config.getBirtTempFileOutputDir();
		developerBirtPath = config.getDeveloperBirtPath();
	}

	/**
	 * Shuts down the BIRT Report Engine
	 */
	@PreDestroy
	public void shutdown() {
		birtReportEngine.destroy();
		Platform.shutdown();
		RegistryProviderFactory.releaseDefault();
		birtReportEngine = null;
	}

	/**
	 * Leggo il report
	 * 1. custom/reportName
	 * 2. parentyTypeId/reportName
	 * 3. reportName
	 *
	 * @param resourceName
	 * @return
	 * @throws RuntimeException
	 */
	public File getReportFromFilesystem(String parentTypeId, String resourceName) throws RuntimeException {
		String reportDirectory = config.getBirtReportInputDir();
		Path birtReport;
		if (developerBirtPath!=null && !developerBirtPath.equals(""))
			birtReport = Paths.get( reportDirectory + File.separator + "project" + File.separator + developerBirtPath + File.separator + resourceName + File.separator + resourceName + ".rptdesign");
		else
			birtReport = Paths.get( reportDirectory + File.separator + "custom" + File.separator + resourceName + File.separator + resourceName + ".rptdesign");
		if (!Files.isReadable(birtReport)) {
			logger.info("Cannot loading rptdesign project/custom: "+birtReport);
			birtReport = Paths.get(reportDirectory + File.separator + parentTypeId + File.separator + resourceName + File.separator + resourceName + ".rptdesign");
			if (!Files.isReadable(birtReport)) {
				logger.info("Cannot loading rptdesign 2nd step: "+birtReport);
				birtReport = Paths.get(reportDirectory + File.separator + resourceName + File.separator + resourceName + ".rptdesign");
				if (!Files.isReadable(birtReport)) {
					logger.info("Cannot loading rptdesign 3th step: "+birtReport);
					throw new RuntimeException("Report " + resourceName + " either did not exist or was not writable.");
				}
			}
		}
		return birtReport.toFile();
	}

	/**
	 * This method creates and executes the report task, the main responsibility
	 * of the entire Report Service. This method is key to enabling pagination
	 * for the BIRT report. The IRunTask run task is created and then used to
	 * generate an ".rptdocument" binary file. This binary file is then read by
	 * the separately created IRenderTask render task. The render task renders
	 * the binary document as a binary PDF output stream which is then returned
	 * from the method.
	 * <p>
	 *
	 * @param birtReport
	 *            the report object created at the controller to hold the data
	 *            of the report request.
	 * @return Returns a ByteArrayOutputStream of the PDF bytes generated by the
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ReportHandler runReport(Report birtReport) {
		OutputStream byteArrayOutputStream;
		File rptDesignFile;
		File reportContentTempFile;
		// get the path to the report design file
		try {
			rptDesignFile = getReportFromFilesystem(birtReport.getType(), birtReport.getName());
		} catch (Exception e) {
			logger.error("Error while loading rptdesign: {}.", e.getMessage());
			throw new RuntimeException("Could not find report");
		}
		Map<String, Object> reportParameters = birtReport.getParameters();
		try {
			reportContentTempFile = new File(reportTempDirectory + File.separator + "birt_report_temp_file_" + birtReport.getTaskId() + ".tmp");
			reportContentTempFile.deleteOnExit();
			byteArrayOutputStream = new FileOutputStream(reportContentTempFile);
			IReportRunnable reportDesign = birtReportEngine.openReportDesign(rptDesignFile.getPath());
			// setting locale
			// see
			// https://stackoverflow.com/questions/25281571/birt-is-not-finding-properties-file-containing-localization-at-runtime-servlet
			try {
				reportDesign.getDesignHandle().setStringProperty("locale", birtReport.getReportLocale().toString());
			} catch (SemanticException e) {
				logger.error("error", e);
				throw new RuntimeException(e);
			}
			IRunTask runTask = birtReportEngine.createRunTask(reportDesign);
			runTask.setLocale(birtReport.reportLocale);
			logger.info("ResourcePath: {}", birtReportEngine.getConfig().getResourcePath());
			runTask.getAppContext().put(BIRT_PARAMETERS, reportParameters);
			if (reportParameters.size() > 0) {
				for (Map.Entry<String, Object> entry : reportParameters.entrySet()) {
					runTask.setParameterValue(entry.getKey(), entry.getValue());
				}
			}
			runTask.validateParameters();
			String rptdocument = reportTempDirectory + File.separator + "generated_" + birtReport.getTaskId()+ ".rptdocument";
			runTask.run(rptdocument);
			IReportDocument reportDocument = birtReportEngine.openReportDocument(rptdocument);
			IRenderTask renderTask = birtReportEngine.createRenderTask(reportDocument);
			renderTask.setLogger(java.util.logging.Logger.getLogger(IRenderTask.class.getSimpleName()));
			renderTask.setProgressMonitor(birtReport.getBirtServiceProgress());
			birtReport.getBirtServiceProgress().setTask(renderTask);
			RenderOption options = new RenderOption();
			String outputFormat = (String) reportParameters.get("outputFormat");
			if ("xlsx".equals(outputFormat)) {
				 EXCELRenderOption excelRenderOption= new EXCELRenderOption();
				 excelRenderOption.setEnableMultipleSheet(true);
			} else if ("doc".equals(outputFormat)) {
				options.setOption(IRenderOption.HTML_PAGINATION, true);
			} else {
				PDFRenderOption pdfRenderOption = new PDFRenderOption();
				pdfRenderOption.setOption(IPDFRenderOption.REPAGINATE_FOR_PDF, Boolean.TRUE);
				pdfRenderOption.closeOutputStreamOnExit(true);
			}
			options.setOutputFormat(outputFormat);			
			options.setOutputStream(byteArrayOutputStream);
			renderTask.setRenderOption(options);
			renderTask.render();
			renderTask.close();
		} catch (EngineException e) {
			logger.error("Error while running report task: {}.", e.getMessage());
			// TODO add custom message to thrown exception
			throw new RuntimeException(e);
		} catch (IOException e) {
			logger.error("Error while running report task: {}. Cannot create temporary file", e.getMessage());
			// TODO add custom message to thrown exception
			throw new RuntimeException(e);
		}
		return new ReportHandler(reportContentTempFile);
	}

}
