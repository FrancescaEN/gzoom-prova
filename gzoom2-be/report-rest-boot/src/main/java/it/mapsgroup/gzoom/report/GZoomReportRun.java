package it.mapsgroup.gzoom.report;

import it.mapsgroup.gzoom.ReportModuleConfiguration;
import it.mapsgroup.gzoom.rest.ReportJobController;
import it.mapsgroup.gzoom.service.ReportJobService;
import it.mapsgroup.gzoom.service.ReportTaskService;
import it.memelabs.smartnebula.spring.boot.config.ApplicationContextProvider;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.endpoint.jmx.JmxEndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.boot.SpringApplication.run;
import org.springframework.boot.actuate.autoconfigure.endpoint.jmx.JmxEndpointAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;

/**
 * @author Andrea Fossi.
 */
@SpringBootApplication(exclude = {
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class,
        SecurityAutoConfiguration.class,
	JmxAutoConfiguration.class,
        JmxEndpointAutoConfiguration.class })

@EnableConfigurationProperties
@ComponentScan(basePackageClasses = {ReportJobService.class, ReportJobController.class})
@Import({ReportModuleConfiguration.class, GZoomReportWebConfig.class})


public class GZoomReportRun {
    private static final Logger LOG = getLogger(GZoomReportRun.class);

    @Bean
    public ApplicationContextProvider applicationContextProvider(ApplicationContext ac) {
        ApplicationContextProvider provider = new ApplicationContextProvider();
        provider.setApplicationContext(ac);
        return provider;
    }

    public static void main(String[] args) throws Exception {
        LOG.info("logging.config [{}])", System.getProperty("logging.config"));
        ConfigurableApplicationContext ctx = run(GZoomReportRun.class, args);
        //resume suspended
        ctx.getBean(ReportTaskService.class).resume();

        Runtime rt = Runtime.getRuntime();
        LOG.info("total memory : " + rt.totalMemory());
        LOG.info("max memory : " + rt.maxMemory());
        LOG.info("free memory : " + rt.freeMemory());
    }


}
