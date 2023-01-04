package org.komamitsu.springtest.data.jdbc.multids;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.BatchJdbcOperations;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.DefaultDataAccessStrategy;
import org.springframework.data.jdbc.core.convert.InsertStrategyFactory;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.core.convert.SqlGeneratorSource;
import org.springframework.data.jdbc.core.convert.SqlParametersFactory;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.MySqlDialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories(basePackages = "org.komamitsu.springtest.data.jdbc.multids.domain.repository.mysql",
    transactionManagerRef = "mysqlTransactionManager",
    jdbcOperationsRef = "mysqlNamedParameterJdbcOperations",
    dataAccessStrategyRef = "mysqlDataAccessStrategy"
)
public class MysqlConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.mysql")
    DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    DataSource mysqlDataSource() {
        return mysqlDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    JdbcTemplate mysqlJdbcTemplate() {
        return new JdbcTemplate(mysqlDataSource());
    }

    @Bean
    NamedParameterJdbcOperations mysqlNamedParameterJdbcOperations() {
        return new NamedParameterJdbcTemplate(mysqlDataSource());
    }

    @Bean
    PlatformTransactionManager mysqlTransactionManager() {
        return new JdbcTransactionManager(mysqlDataSource());
    }

    @Bean
    DataAccessStrategy mysqlDataAccessStrategy(
        RelationalMappingContext context, JdbcConverter converter) {

        NamedParameterJdbcOperations operations = mysqlNamedParameterJdbcOperations();

        Dialect dialect = MySqlDialect.INSTANCE;
        return new DefaultDataAccessStrategy(
            new SqlGeneratorSource(context, converter, dialect),
            context,
            converter,
            operations,
            new SqlParametersFactory(context, converter, dialect),
            new InsertStrategyFactory(operations, new BatchJdbcOperations(mysqlJdbcTemplate()), dialect));
    }
}
