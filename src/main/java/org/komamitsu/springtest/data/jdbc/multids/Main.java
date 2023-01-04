package org.komamitsu.springtest.data.jdbc.multids;

import java.util.Iterator;
import org.komamitsu.springtest.data.jdbc.multids.domain.repository.postgresql.PostgresqlPlayerRepository;
import org.komamitsu.springtest.data.jdbc.multids.domain.model.Player;
import org.komamitsu.springtest.data.jdbc.multids.domain.repository.mysql.MysqlPlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Autowired
    @Qualifier("postgresqlJdbcTemplate")
    JdbcTemplate postgresqlTemplate;

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    JdbcTemplate mysqlTemplate;

    @Autowired
    PostgresqlPlayerRepository postgresqlRepository;

    @Autowired
    MysqlPlayerRepository mysqlRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    private void setUpSchema() throws IOException {
        {
            ClassPathResource resource = new ClassPathResource("pg-schema.sql");
            Files.readAllLines(Paths.get(resource.getURI()))
                .forEach(ddl -> postgresqlTemplate.execute(ddl));
        }
        {
            ClassPathResource resource = new ClassPathResource("mysql-schema.sql");
            Files.readAllLines(Paths.get(resource.getURI()))
                .forEach(ddl -> mysqlTemplate.execute(ddl));
        }
    }

    @Bean
    public CommandLineRunner run() throws Exception {
        return (String[] args) -> {
            setUpSchema();

            String postgresqlUserName = "postgresql";
            String mysqlUserName = "mysql";

            postgresqlRepository.save(new Player(null, postgresqlUserName));
            mysqlRepository.save(new Player(null, mysqlUserName));

            {
                Iterator<Player> iterator = postgresqlRepository.findAll().iterator();
                String name = iterator.next().name;
                if (!name.equals(postgresqlUserName)) {
                    throw new AssertionError(
                        String.format("Expected %s stored in PostgreSQL datasource, but got %s", postgresqlUserName, name));
                }
                if (iterator.hasNext()) {
                    throw new AssertionError("PostgreSQL datasource has multiple records unexpectedly");
                }
            }

            {
                Iterator<Player> iterator = mysqlRepository.findAll().iterator();
                String name = iterator.next().name;
                if (!name.equals(mysqlUserName)) {
                    throw new AssertionError(
                        String.format("Expected %s stored in MySQL datasource, but got %s", mysqlUserName, name));
                }
                if (iterator.hasNext()) {
                    throw new AssertionError("MySQL datasource has multiple records unexpectedly");
                }
            }
        };
    }
}
