package it.memelabs.gn.xmlrpc;

import org.apache.xmlrpc.parser.AtomicParser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.Date;

/**
 * 27/11/13
 *
 * @author Andrea Fossi
 */
public class TimestampParser extends AtomicParser {
    //  private static final XsDateTimeFormat format = new XsDateTimeFormat();

    protected void setResult(String pResult) throws SAXException {
        try {
            super.setResult(new Date(Long.parseLong(pResult)));
        } catch (NumberFormatException e) {
            throw new SAXParseException("Failed to parse GnTimestamp value: [" + pResult + "]",
                    getDocumentLocator());
        }

   /*     try {
            Object pResult1 = format.parseObject(pResult.trim());
            Timestamp timestamp = new Timestamp(((Calendar) pResult1).getTime().getTime());
            super.setResult(timestamp);
        } catch (ParseException e) {
            int offset = e.getErrorOffset();
            final String msg;
            if (offset == -1) {
                msg = "Failed to parse dateTime value: " + pResult;
            } else {
                msg = "Failed to parse dateTime value " + pResult
                        + " at position " + e.getErrorOffset();
            }
            throw new SAXParseException(msg, getDocumentLocator(), e);
        }*/
    }
}
