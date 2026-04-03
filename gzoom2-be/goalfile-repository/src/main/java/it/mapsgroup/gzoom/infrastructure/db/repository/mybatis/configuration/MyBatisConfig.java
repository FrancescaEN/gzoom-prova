package it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.configuration;

import org.apache.ibatis.mapping.VendorDatabaseIdProvider;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * MyBatis configuration
 */
@Configuration
@MapperScan("it.mapsgroup.gzoom.infrastructure.db.repository.mybatis.mapper")
public class MyBatisConfig {

    /**
     * Defines a bean of type ConfigurationCustomizer to customize MyBatis configuration settings.
     * This customizer enables caching and configures MyBatis to automatically map
     * database column names using underscores (e.g., "user_name") to camelCase
     * properties in Java (e.g., "userName").
     *
     * @return a customized ConfigurationCustomizer for MyBatis
     */
    @Bean
    ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.setCacheEnabled(true);
            configuration.setMapUnderscoreToCamelCase(true);

        };
    }

    /**
     * Defines a bean of type VendorDatabaseIdProvider that maps the database ID
     * to the corresponding specific vendor. This is used to configure the database ID
     * based on the type of vendor, allowing the persistence framework to identify the database.
     *
     * @return a configured instance of VendorDatabaseIdProvider
     */
    @Bean
    VendorDatabaseIdProvider databaseIdProvider() {
        VendorDatabaseIdProvider databaseIdProvider = new VendorDatabaseIdProvider();
        Properties properties = new Properties();

        properties.setProperty("MySQL", "mysql");
        properties.setProperty("Oracle", "oracle");
        properties.setProperty("Microsoft SQL Server", "sqlserver");
        properties.setProperty("PostgreSQL", "postgres");
        properties.put("H2", "h2");
        properties.put("DB2", "db2");

        databaseIdProvider.setProperties(properties);
        return databaseIdProvider;
    }


}