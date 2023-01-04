package org.komamitsu.springtest.data.jdbc.multids;

import org.komamitsu.springtest.data.jdbc.multids.domain.repository.postgresql.PostgresqlUserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories(
    basePackages = "org.komamitsu.springtest.data.jdbc.multids.domain.repository.postgresql",
    transactionManagerRef = "postgresqlTransactionManager",
    jdbcOperationsRef = "postgresqlNamedParameterJdbcOperations"
)
@ConditionalOnMissingBean(PostgresqlUserRepository.class)
public class PostgresqlConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.postgresql")
    public DataSourceProperties postgresqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource postgresqlDataSource() {
        return postgresqlDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public JdbcTemplate postgresqlJdbcTemplate() {
        return new JdbcTemplate(postgresqlDataSource());
    }

    @Bean
    @Primary
    public NamedParameterJdbcOperations postgresqlNamedParameterJdbcOperations() {
        return new NamedParameterJdbcTemplate(postgresqlDataSource());
    }

    @Bean
    public PlatformTransactionManager postgresqlTransactionManager() {
        return new JdbcTransactionManager(postgresqlDataSource());
    }
}
