package it.mapsgroup.gzoom.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
public class LocalDateTest {
    private static final Logger LOG = getLogger(LocalDateTest.class);

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }

    @Test
    public void localDateTest() throws Exception {
        //LocalDate d = LocalDate.parse("2018-02-28", DateTimeFormatter.ISO_DATE);
        //DateTimeFormatter.ISO_DATE.format(value);

        String json = jsonTest("2018-02-01", "2018-02-28");
        LOG.info(json);
        json = jsonTest("2016-02-01", "2016-02-29");
        LOG.info(json);
        json = jsonTest("2016-09-22", "2016-10-31");
        LOG.info(json);

    }

    private String jsonTest(String startDate, String endDate) throws java.io.IOException {
        SimpleBean bean = new SimpleBean();
        bean.setFromDate(LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE));
        bean.setToDate(LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE));
        String json = objectMapper.writeValueAsString(bean);
        Map map = objectMapper.readValue(json, Map.class);
        assertEquals(map.get("fromDate"), startDate);
        assertEquals(map.get("toDate"), endDate);

        SimpleBean bean2 = objectMapper.readValue(json, SimpleBean.class);
        assertTrue(bean2.getFromDate().equals(bean.getFromDate()));
        assertTrue(bean2.getToDate().equals(bean.getToDate()));
        return json;
    }
}
