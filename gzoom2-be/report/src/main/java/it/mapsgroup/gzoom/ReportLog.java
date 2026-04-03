package it.mapsgroup.gzoom;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

public class ReportLog {
	private static final Logger logger = getLogger(ReportLog.class);
	
	public static void info(String info) {
		logger.info(info);
	}
	
	public static void debug(String debug) {
		logger.debug(debug);
	}
}
