package com.epam.esm.config;


import com.epam.esm.domain.entity.mapper.CertificateMapper;
import com.epam.esm.domain.entity.mapper.RequestParametersMapper;
import com.epam.esm.repository.api.CertificateRepository;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.repository.impl.CertificateRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("file:../application/src/main/resources/persistence-test.properties")
public class ConfigApplicationContextRepository {

    private final Environment environment;

    public ConfigApplicationContextRepository(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty("url"));
        driverManagerDataSource.setUsername(environment.getProperty("db_user"));
        driverManagerDataSource.setPassword(environment.getProperty("db_pwd"));
        driverManagerDataSource.setDriverClassName(environment.getProperty("driver"));
        return driverManagerDataSource;
    }

//    @Bean
//    public DataSource dataSource() {
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.HSQL)
//                .addScript("classpath:com/bank/config/sql/schema.sql")
//                .addScript("classpath:com/bank/config/sql/test-data.sql")
//                .build();
//    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public CertificateMapper certificateMapper() {
        return new CertificateMapper();
    }

    @Bean
    public CertificateRepository certificateRepository() {
        return new CertificateRepositoryImpl(certificateMapper(), dataSource());
    }
}
