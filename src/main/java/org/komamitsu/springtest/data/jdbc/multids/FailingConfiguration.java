package org.komamitsu.springtest.data.jdbc.multids;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories(basePackages = "org.komamitsu.springtest.data.jdbc.multids.failingdomain",
        transactionManagerRef = "failingTransactionManager",
        jdbcOperationsRef = "failingNamedParameterJdbcOperations"
)
public class FailingConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.failing")
    public DataSourceProperties failingDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource failingDataSource() {
        return failingDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public JdbcTemplate failingJdbcTemplate() {
        return new JdbcTemplate(failingDataSource());
    }

    @Bean
    public NamedParameterJdbcOperations failingNamedParameterJdbcOperations() {
        return new NamedParameterJdbcTemplate(failingDataSource());
    }

    @Bean
    public PlatformTransactionManager failingTransactionManager() {
        return new JdbcTransactionManager(failingDataSource());
    }
}
