package com.bid.bservice.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class H2Datasource {
	
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.h2")
    public DataSourceProperties DefaultH2DataSourceProperties() {
        return new DataSourceProperties();
    }
    
    @Bean(name = "DefaultH2DataSource")
    @Primary
    public DataSource DefaultH2DataSource() {
        return DefaultH2DataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }
    
    @Bean(name="DefaultH2NamedParameterJdbcTemplate")
    @Primary
    public NamedParameterJdbcTemplate defaultH2NamedParameterJdbcTemplate(@Qualifier("DefaultH2DataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean(name="DefaultH2JdbcTemplate")
    @Primary
    public JdbcTemplate defaultH2JdbcTemplate(@Qualifier("DefaultH2DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }  
}
