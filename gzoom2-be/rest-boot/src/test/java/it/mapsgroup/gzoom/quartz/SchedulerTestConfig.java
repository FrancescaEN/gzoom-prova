package it.mapsgroup.gzoom.quartz;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mapsgroup.gzoom.persistence.common.CommonPersistenceConfiguration;
import it.mapsgroup.gzoom.report.service.ReportCallbackService;
import it.mapsgroup.gzoom.service.GzoomReportClientConfig;
import it.mapsgroup.gzoom.service.report.ReportClientService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Andrea Fossi.
 */
@Configuration
@ComponentScan(basePackageClasses = {})
@ComponentScan(basePackageClasses = ReportCallbackService.class)
@Import({
        CommonPersistenceConfiguration.class,
        QuartzConfiguration.class,
})
public class SchedulerTestConfig {
    private static final Logger LOG = getLogger(SchedulerTestConfig.class);

    @Bean
    public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
                //.registerModule(new ParameterNamesModule())
                //.registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new ISO8601DateFormat());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }
    
    @Autowired
    private GzoomReportClientConfig config;
    
    @Bean
    public ReportClientService reportClientService() {
        return new ReportClientService(new RestTemplate(), config);
    }
/*
    @Bean
    GzoomReportClientConfig gzoomReportClientConfig(Environment env) {
        return () -> {
            try {
                String url = env.getProperty("gzoom.server.report.url");
                LOG.info("gzoom.server.report.url=" + url);
                return new URL(url);
            } catch (MalformedURLException e) {
                LOG.error("URL parsing error", e);
                throw new RuntimeException(e);
            }
        };
    }*/

   /* @Bean
    SchedulerConfig schedulerConfig() {
        return new SchedulerConfig() {
            @Override
            public int getReportProbeDelay() {
                return 10;
            }

            @Override
            public int getReportProbeRetries() {
                return 240;
            }
        };
    }*/

}
