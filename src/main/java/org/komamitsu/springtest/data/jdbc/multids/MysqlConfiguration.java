package org.komamitsu.springtest.data.jdbc.multids;

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
@EnableJdbcRepositories(basePackages = "org.komamitsu.springtest.data.jdbc.multids.domain.repository.mysql",
    transactionManagerRef = "mysqlTransactionManager",
    jdbcOperationsRef = "mysqlNamedParameterJdbcOperations"
)
public class MysqlConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.mysql")
    DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource mysqlDataSource() {
        return mysqlDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public JdbcTemplate mysqlJdbcTemplate() {
        return new JdbcTemplate(mysqlDataSource());
    }

    @Bean
    public NamedParameterJdbcOperations mysqlNamedParameterJdbcOperations() {
        return new NamedParameterJdbcTemplate(mysqlDataSource());
    }

    @Bean
    public PlatformTransactionManager mysqlTransactionManager() {
        return new JdbcTransactionManager(mysqlDataSource());
    }
}
