package it.mapsgroup.gzoom.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;
import java.util.Properties;


/**
 * @author Andrea Fossi.
 * @author Leonardo Minaudo
 */
@Configuration
@ConditionalOnProperty(name = "gzoom.scheduler.enable", havingValue = "true")
@ComponentScan(basePackageClasses = QuartzConfiguration.class)
public class QuartzConfiguration {
    @Autowired
    private ApplicationContext applicationContext;


    @Bean
    public SchedulerFactoryBean schedulerFactory(Environment environment, ApplicationContext appCtx, @Qualifier("mainDataSource") DataSource mainDataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setConfigLocation(new ClassPathResource("/scheduler/quartz.properties"));
        factory.setDataSource(mainDataSource);
        Properties properties = new Properties();
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", environment.getProperty("gzoom.quartz.jobStore.driverDelegateClass", String.class, "org.quartz.impl.jdbcjobstore.StdJDBCDelegate"));
        factory.setQuartzProperties(properties);
        factory.setJobFactory(springBeanJobFactory());
        return factory;
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringJobFactory jobFactory = new AutowiringJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
}
