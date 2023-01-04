package org.komamitsu.springtest.data.jdbc.multids;

import org.komamitsu.springtest.data.jdbc.multids.domain.repository.postgresql.PostgresqlPlayerRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.core.convert.BatchJdbcOperations;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.convert.InsertStrategyFactory;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.convert.SqlGeneratorSource;
import org.springframework.data.jdbc.core.convert.SqlParametersFactory;
import org.springframework.data.jdbc.core.dialect.JdbcPostgresDialect;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
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
    jdbcOperationsRef = "postgresqlNamedParameterJdbcOperations",
    dataAccessStrategyRef = "postgresqlDataAccessStrategy"
)
@ConditionalOnMissingBean(PostgresqlPlayerRepository.class)
public class PostgresqlConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.postgresql")
    DataSourceProperties postgresqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    DataSource postgresqlDataSource() {
        return postgresqlDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    JdbcTemplate postgresqlJdbcTemplate() {
        return new JdbcTemplate(postgresqlDataSource());
    }

    @Bean
    @Primary
    NamedParameterJdbcOperations postgresqlNamedParameterJdbcOperations() {
        return new NamedParameterJdbcTemplate(postgresqlDataSource());
    }

    @Bean
    PlatformTransactionManager postgresqlTransactionManager() {
        return new JdbcTransactionManager(postgresqlDataSource());
    }

    @Bean
    @Primary
    DataAccessStrategy postgresqlDataAccessStrategy(
        RelationalMappingContext context, JdbcConverter converter) {

        NamedParameterJdbcOperations operations = postgresqlNamedParameterJdbcOperations();

        Dialect dialect = JdbcPostgresDialect.INSTANCE;
        return new DefaultDataAccessStrategy(
            new SqlGeneratorSource(context, converter, dialect),
            context,
            converter,
            operations,
            new SqlParametersFactory(context, converter, dialect),
            new InsertStrategyFactory(operations, new BatchJdbcOperations(postgresqlJdbcTemplate()), dialect));
    }
}
