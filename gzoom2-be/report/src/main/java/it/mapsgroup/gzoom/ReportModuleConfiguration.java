package it.mapsgroup.gzoom;

import it.mapsgroup.gzoom.birt.BirtService;
import it.mapsgroup.gzoom.jasper.JasperService;
import it.mapsgroup.gzoom.mybatis.MyBatisPersistenceConfiguration;
import it.mapsgroup.gzoom.persistence.common.CommonPersistenceConfiguration;
import it.mapsgroup.gzoom.mybatis.dao.ReportActivityDao;
import it.mapsgroup.gzoom.service.JasperReportTaskService;
import it.mapsgroup.gzoom.service.ReportTaskService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * @author Andrea Fossi.
 */
@Configuration
@ImportResource("classpath:app-config.xml")
@Import({
        MyBatisPersistenceConfiguration.class,
        CommonPersistenceConfiguration.class
})
/*
@ComponentScan(basePackageClasses = {
        ReportTaskService.class,
        BirtService.class,
        ReportActivityDao.class})
*/

@ComponentScan(basePackageClasses = {
        ReportTaskService.class, JasperReportTaskService.class,
        BirtService.class, JasperService.class,
        ReportActivityDao.class})

public class ReportModuleConfiguration {
}
