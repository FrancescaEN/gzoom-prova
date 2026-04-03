package it.mapsgroup.gzoom.birt;

import org.eclipse.birt.report.engine.api.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.mapsgroup.gzoom.mybatis.dto.ReportParam;
import it.mapsgroup.gzoom.mybatis.dto.ReportParams;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class BIRTReportParams  {

	private Logger logger = getLogger(BIRTReportParams.class);

	private IReportEngine birtReportEngine = null;

	private final BirtConfig config;

	@Autowired
	public BIRTReportParams(BirtConfig config) {
		this.config = config;
	}
	
	/**
	 * Leggo la lista di parametri dal report
	 * @param birtReport
	 * @return
	 */
	public ReportParams getReportParams(Report birtReport) {
		ReportParams reportParameters = new ReportParams();
		File rptDesignFile;

		// get the path to the report design file
		try {
			rptDesignFile = getReportFromFilesystem(birtReport.getName());
		} catch (Exception e) {
			logger.error("Error while loading rptdesign: {}.", e.getMessage());
			throw new RuntimeException("Could not find report");
		}

		try {
			IReportRunnable reportDesign = birtReportEngine.openReportDesign(rptDesignFile.getPath());
			IGetParameterDefinitionTask task = birtReportEngine.createGetParameterDefinitionTask(reportDesign);
			
			Collection<?> params = task.getParameterDefns(true);
			logger.info("---> params", params);
			reportParameters.setParams(this.conversionParam(params));

		} catch (EngineException e) {
			logger.error("Error while running report params task: {}.", e.getMessage());
			throw new RuntimeException(e);
		}
			
		return reportParameters;
	}
	
	private File getReportFromFilesystem(String reportName) throws RuntimeException {
		String reportDirectory = config.getBirtReportInputDir();
		Path birtReport = Paths.get(reportDirectory + File.separator + reportName + File.separator + reportName + ".rptdesign");
		if (!Files.isReadable(birtReport))
			throw new RuntimeException("Report " + reportName + " either did not exist or was not writable.");

		return birtReport.toFile();
	}
	

	private List<ReportParam> conversionParam(Collection<?> c) {
		List<ReportParam> parmDetails = new ArrayList<ReportParam>();
    	Iterator<?> iterator = c.iterator();
    	while(iterator.hasNext()) {
    		IParameterDefnBase param = (IParameterDefnBase) iterator.next( );
    		if (param instanceof IParameterGroupDefn) {
    			//attualmente noi non abbiamo GRUPPI
	    		IParameterGroupDefn group = (IParameterGroupDefn) param;
	    		logger.info("Parameter Group: ", group.getName() );
	    		Iterator<?> i2 = group.getContents( ).iterator( );
	    		while (i2.hasNext()) {
	    			IScalarParameterDefn scalar = (IScalarParameterDefn) i2.next( );
		    		parmDetails.add(loadParameterDetails(scalar, group));
	    		}
    		} else {
	    		IScalarParameterDefn scalar = (IScalarParameterDefn) param;
	    		parmDetails.add(loadParameterDetails(scalar, null));	
    		}    		
    	}    	
    	return parmDetails;
    }

	//TODO decidere cosa mi serve
	private ReportParam loadParameterDetails(IScalarParameterDefn scalar, IParameterGroupDefn group) {
		ReportParam param = new ReportParam();
		if (!scalar.isHidden()) {
			param.setParamName(scalar.getName());
			param.setMandatory(scalar.isRequired());
			param.setParamDefault(scalar.getDefaultValue());
			param.setParamType("INPUT"); //??
			return param;
		}
		return null;
		/*
		if (group == null) {
			parameter.put("Parameter Group", "Default");
		} else {
			parameter.put("Parameter Group", group.getName());
		}
		parameter.put("Name", scalar.getName());
		parameter.put("Display Name", scalar.getDisplayName());
		parameter.put("Display Format", scalar.getDisplayFormat());

		parameter.put("Hidden", scalar.isHidden());
		parameter.put("Allow Blank", scalar.allowBlank());
		parameter.put("Allow Null", scalar.allowNull());
		parameter.put("Required", scalar.isRequired());
		parameter.put("Default Value", scalar.getDefaultValue());
		
		switch (scalar.getControlType()) {
		case IScalarParameterDefn.TEXT_BOX:
			parameter.put("Type", "Text Box");
			break;
		case IScalarParameterDefn.LIST_BOX:
			parameter.put("Type", "List Box");
			break;
		case IScalarParameterDefn.RADIO_BUTTON:
			parameter.put("Type", "List Box");
			break;
		case IScalarParameterDefn.CHECK_BOX:
			parameter.put("Type", "List  Box");
			break;
		default:
			parameter.put("Type", "Text Box");
			break;
		}

		switch (scalar.getDataType()) {
		case IScalarParameterDefn.TYPE_STRING:
			parameter.put("Data Type", "String");
			break;
		case IScalarParameterDefn.TYPE_FLOAT:
			parameter.put("Data Type", "Float");
			break;
		case IScalarParameterDefn.TYPE_DECIMAL:
			parameter.put("Data Type", "Decimal");
			break;
		case IScalarParameterDefn.TYPE_DATE_TIME:
			parameter.put("Data Type", "Date Time");
			break;
		case IScalarParameterDefn.TYPE_BOOLEAN:
			parameter.put("Data Type", "Boolean");
			break;
		default:
			parameter.put("Data Type", "Any");
			break;
		}

		ScalarParameterHandle parameterHandle = (ScalarParameterHandle) scalar.getHandle();
		parameter.put("Default Value", scalar.getDefaultValue());
		parameter.put("Prompt Text", scalar.getPromptText());
		parameter.put("Data Set Expression", parameterHandle.getValueExpr());
		
		
		Iterator<String> iter = parameter.keySet().iterator();
		String mapString = "";
		while (iter.hasNext()) {
			String name = (String) iter.next();
			mapString += name + " = " + parameter.get(name) + ", ";
		}
		logger.info("====================== Parameter = " + scalar.getName() + ": " + mapString);*/

	}

}
