package org.komamitsu.springtest.data.jdbc.multids;

import org.komamitsu.springtest.data.jdbc.multids.defaultdomain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
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
        basePackages = "org.komamitsu.springtest.data.jdbc.multids.defaultdomain",
        jdbcOperationsRef = "defaultNamedParameterJdbcOperations"
)
@ConditionalOnMissingBean(UserRepository.class)
public class DefaultConfiguration {
    @Bean
    @ConfigurationProperties("spring.datasource.default")
    public DataSourceProperties defaultDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource defaultDataSource() {
        return defaultDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    public JdbcTemplate defaultJdbcTemplate() {
        return new JdbcTemplate(defaultDataSource());
    }

    @Bean
    @Primary
    public NamedParameterJdbcOperations defaultNamedParameterJdbcOperations() {
        return new NamedParameterJdbcTemplate(defaultDataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JdbcTransactionManager(defaultDataSource());
    }
}
