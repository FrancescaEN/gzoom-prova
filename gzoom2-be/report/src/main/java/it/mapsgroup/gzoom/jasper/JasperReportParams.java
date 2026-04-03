package it.mapsgroup.gzoom.jasper;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class JasperReportParams {

	private Logger logger = getLogger(JasperReportParams.class);
	private final JasperConfig config;

	@Autowired
	public JasperReportParams(JasperConfig config) {
		this.config = config;
	}
}
