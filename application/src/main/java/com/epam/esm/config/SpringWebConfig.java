package com.epam.esm.config;


import com.epam.esm.exceptions.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.epam.esm"})
@PropertySource("classpath:messages.properties")
@PropertySource("classpath:messages_uk.properties")
@PropertySource("classpath:messages_en.properties")
@PropertySource("classpath:persistence-${envTarget:prod}.properties")
public class SpringWebConfig implements WebMvcConfigurer {

    private final Environment environment;

    @Autowired
    public SpringWebConfig(Environment environment) {
        this.environment = environment;
    }

    /**
     * Set the list of resource files here
     *
     * @return resourceBundleMessageSource
     */
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasenames("messages");
        rs.setDefaultEncoding("UTF-8");
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

    @Bean
    public Translator translator() {
        return new Translator(messageSource());
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setUrl(environment.getProperty("url"));
        driverManagerDataSource.setUsername(environment.getProperty("db_user"));
        driverManagerDataSource.setPassword(environment.getProperty("db_pwd"));
        driverManagerDataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("driver")));
        return driverManagerDataSource;
    }

    @Bean public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        WebMvcConfigurer.super.configureMessageConverters(messageConverters);
    }

    /**
     * Setup a simple strategy: use all the defaults and return XML by default when not sure.
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }
}
