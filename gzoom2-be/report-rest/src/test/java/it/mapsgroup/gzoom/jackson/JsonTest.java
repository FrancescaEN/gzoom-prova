package it.mapsgroup.gzoom.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.mapsgroup.gzoom.report.report.dto.CreateReport;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
public class JsonTest {
private static final Logger LOG = getLogger(JsonTest.class);

    @Test
    public void name() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper()
             /*   .enableDefaultTyping(
                ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT,
                JsonTypeInfo.As.PROPERTY)*/
                ;
        CreateReport report = new CreateReport();
        Date dateToSerialize = new Date();
        report.getParams().put("dateTest", dateToSerialize);
        String s = objectMapper.writeValueAsString(report);
        LOG.debug(s);
        CreateReport deserializedItem = objectMapper.readValue(s, CreateReport.class);
        assertEquals(dateToSerialize,deserializedItem.getParams().values().iterator().next());
    }
}
