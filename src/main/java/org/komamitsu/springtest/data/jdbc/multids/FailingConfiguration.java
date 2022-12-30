package org.komamitsu.springtest.data.jdbc.multids;

import org.komamitsu.springtest.data.jdbc.multids.defaultdomain.repository.UserRepository;
import org.komamitsu.springtest.data.jdbc.multids.failingdomain.repository.FailingUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories(basePackages = "org.komamitsu.springtest.data.jdbc.multids.failingdomain",
        transactionManagerRef = "failingTransactionManager")
public class FailingConfiguration {
    @Autowired
    FailingUserRepository failingUserRepository;

    @Bean("myFailingUserRepository")
    public FailingUserRepository failingUserRepository() {
        return failingUserRepository;
    }

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
    public JdbcTemplate failingJdbcTemplate(@Qualifier("failingDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PlatformTransactionManager failingTransactionManager(
            @Qualifier("failingDataSource") DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
}
