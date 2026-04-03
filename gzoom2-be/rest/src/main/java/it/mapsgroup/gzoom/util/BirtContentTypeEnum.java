package it.mapsgroup.gzoom.util;

import java.util.Map;

import java.util.Hashtable;

/**
 * Enumeration for permission prefix
 * @author asma
 *
 */
public enum BirtContentTypeEnum {
	
    pdf("pdf", "application/pdf"),
    xls("xls", "application/vnd.ms-excel"),
    doc("doc", "application/vnd.ms-word"),
    xlsx("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	
    private final String outputFormat;
	
	private final String contentType;
	
	/**
	 * Constructor
	 * @param outputFormat
	 * @param contentType
	 */
	BirtContentTypeEnum(String outputFormat, String contentType) {
		this.outputFormat = outputFormat;
		this.contentType = contentType;
	}

	/**
	 * @return the outputFormat
	 */
	public String getOutputFormat() {
		return outputFormat;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	
	private static final Map<String, String> CONTENT_TYPE_MAP = new Hashtable<String, String>();
	
	static {
        for (BirtContentTypeEnum ss : values()) {
        	CONTENT_TYPE_MAP.put(ss.outputFormat, ss.contentType);
        }
	}
	
	/**
	 * gives the permissionPrefix associated to code
	 * @param code
	 * @return
	 */
	public static String getContentType(String outputFormat) {
		return CONTENT_TYPE_MAP.get(outputFormat);
	}

}
