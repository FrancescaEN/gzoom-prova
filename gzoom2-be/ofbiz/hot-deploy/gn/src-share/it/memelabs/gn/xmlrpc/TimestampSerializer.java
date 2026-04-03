package it.memelabs.gn.xmlrpc;

import org.apache.xmlrpc.serializer.TypeSerializerImpl;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.util.Date;

/**
 * 27/11/13
 *
 * @author Andrea Fossi
 */
public class TimestampSerializer extends TypeSerializerImpl {
    // CalendarSerializer calendarSerializer = new CalendarSerializer();

    // private static final XsDateTimeFormat format = new XsDateTimeFormat();

    /**
     * Tag name of a BigDecimal value.
     */
    public static final String TIMESTAMP_TAG = "gntimestamp";

    private static final String EX_TIMESTAMP_TAG = "ex:" + TIMESTAMP_TAG;

    @Override
    public void write(ContentHandler pHandler, Object pObject) throws SAXException {
        assert pObject instanceof Date;
        write(pHandler, TIMESTAMP_TAG, EX_TIMESTAMP_TAG, Long.toString(((Date) pObject).getTime()));
       /* Calendar calendar = Calendar.getInstance();
        if (pObject instanceof Timestamp) {
            Timestamp timestamp = (Timestamp) pObject;
            calendar.setTimeInMillis(timestamp.getTime());
        } else if (pObject instanceof Date) {
            Date timestamp = (Date) pObject;
            calendar.setTimeInMillis(timestamp.getTime());
        }
        write(pHandler, TIMESTAMP_TAG, EX_TIMESTAMP_TAG, format.format(calendar));
        */
    }
}
