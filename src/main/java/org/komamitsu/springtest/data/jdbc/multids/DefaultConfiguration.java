package org.komamitsu.springtest.data.jdbc.multids;

import org.komamitsu.springtest.data.jdbc.multids.defaultdomain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJdbcRepositories(basePackages = "org.komamitsu.springtest.data.jdbc.multids.defaultdomain.repository")
@ConditionalOnMissingBean(UserRepository.class)
public class DefaultConfiguration {
    @Autowired UserRepository userRepository;

    @Bean("myUserRepository")
    @Lazy
    public UserRepository userRepository() {
        return userRepository;
    }

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

//    @Bean
//    @Primary
//    public JdbcTemplate defaultJdbcTemplate(@Qualifier("defaultDataSource") DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }

    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier("defaultDataSource") DataSource dataSource) {
        return new JdbcTransactionManager(dataSource);
    }
}
