package it.mapsgroup.gzoom.util;

import it.mapsgroup.gzoom.commons.collect.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Manifest utilities.
 *
 * @author Fabio Strozzi
 */
public class Manifests {
    private static final Logger LOG = LoggerFactory.getLogger(Manifests.class);

    private static final String IMPLEMENTATION_TITLE = "Implementation-Title";
    private static final String IMPLEMENTATION_VERSION = "Implementation-Version";
    private static final String IMPLEMENTATION_DATE = "Implementation-Date";

    private Manifests() {}

    /**
     * Retrieves product information from the MANIFEST.MF file.
     *
     * @param res The {@link Supplier} of {@link Manifest} used to read the MANIFEST.MF file
     * @param implementationTitle Implementation-Title to read in the MANIFEST.MF file
     * @return A tuple of two values: the implementation version and the implementation date.
     */
    public static Tuple2<String, Date> getProductInfo(Supplier<Manifest> res, String implementationTitle) {
        String version = null;
        Date date = null;
        try {
            Manifest manifest = res.get();
            Attributes attribs = manifest.getMainAttributes();
            String title = attribs.getValue("Implementation-Title");
            // LOG.info("enum Implementation-Title " + title);
            if (implementationTitle.equals(title)) {
                version = initProductVersion(attribs);
                LOG.info("getProductInfo" + version);
                date = initProductDate(attribs);
                LOG.info("getProductInfo" + date);
            }
        } catch (Exception e) {
            LOG.warn("Cannot read MANIFEST.MF file, setting sample values", e);
            date = new Date();
            version = "?";
        }

        return Tuple2.of(version, date);
    }

    private static String initProductVersion(Attributes attribs) {
        String v = attribs.getValue(IMPLEMENTATION_VERSION);
        if (v == null) {
            LOG.error("Cannot read product version from MANIFEST.MF");
            v = "?";
        }
        return v;
    }

    private static Date initProductDate(Attributes attribs) {
        String ts = attribs.getValue(IMPLEMENTATION_DATE);
        if (ts == null) {
            LOG.error("Cannot read product date from MANIFEST.MF");
            return new Date();
        }
        try {
            // maven timestamp format is yyyyMMdd-hhmm (20130424-1231)
            return new SimpleDateFormat("yyyyMMdd-HHmm").parse(ts);
        } catch (ParseException e) {
            LOG.error("Invalid product date format in MANIFEST.MF");
            return new Date();
        }
    }
}
