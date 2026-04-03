package it.mapsgroup.gzoom;


import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.boot.SpringApplication.run;
import org.springframework.boot.actuate.autoconfigure.endpoint.jmx.JmxEndpointAutoConfiguration;

/**
 * @author Andrea Fossi.
 */

@ImportResource("classpath:/bootstrap-context.xml")
@SpringBootApplication(exclude = {
        JmxAutoConfiguration.class,
	JmxEndpointAutoConfiguration.class,
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})
@EnableConfigurationProperties
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)

public class GZoomRun {
    private static final Logger LOG = getLogger(GZoomRun.class);

    public static void main(String[] args) throws Exception {
        LOG.info("logging.config [{}])", System.getProperty("logging.config"));

//        if (StringUtils.isEmpty(System.getProperty("logging.config")))
//            System.setProperty("logging.config", System.getProperty("gzoom.conf.dir") + "/logback.xml");
//        LOG.info("logging.config [{}])", System.getProperty("logging.config"));
//        //SpringApplication.run(LmmRun.class, args);
//        new SpringApplicationBuilder(GZoomRun.class)
//                .initializers(new PropertyApplicationContextInitializer("file:" + System.getProperty("gzoom.conf.dir") + "/gzoom.properties"))
//                .run(args);
         run(GZoomRun.class, args);
    }

}
