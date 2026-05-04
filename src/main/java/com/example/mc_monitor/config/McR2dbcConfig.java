package com.example.mc_monitor.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.MySqlDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;

import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@Slf4j
@EnableR2dbcRepositories(basePackages = "com.example.mc_monitor.repository", entityOperationsRef = "mcR2dbcTemplate")
public class McR2dbcConfig {

    @Value("${spring.r2dbc.mcDatabaseInfo.dbType}")
    private String mcDbType;

    @Value("${spring.r2dbc.mcDatabaseInfo.host}")
    private String mcHost;

    @Value("${spring.r2dbc.mcDatabaseInfo.port}")
    private int mcPort;

    @Value("${spring.r2dbc.mcDatabaseInfo.dbName}")
    private String mcDbName;

    @Value("${spring.r2dbc.mcDatabaseInfo.username}")
    private String mcUserName;

    @Value("${spring.r2dbc.mcDatabaseInfo.password}")
    private String mcPassword;

    @Value("${spring.r2dbc.mcDatabaseInfo.pool.initial-size}")
    private int initialSize;

    @Value("${spring.r2dbc.mcDatabaseInfo.pool.max-size}")
    private int maxSize;

    @Bean
    @Qualifier("mcConnectionFactory")
    public ConnectionFactory mcConnectionFactory() {
        log.info("mc Database Connection Define : {}@{}", mcUserName, mcHost);
        ConnectionFactory connectionFactory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
                .option(DRIVER, mcDbType)
                .option(HOST, mcHost)
                .option(PORT, mcPort)
                .option(USER, mcUserName)
                .option(PASSWORD, mcPassword)
                .option(DATABASE, mcDbName)
                .build());

        ConnectionPoolConfiguration connectionPoolConfiguration = ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMinutes(30))
                .initialSize(initialSize)
                .maxSize(maxSize)
                .validationQuery("SELECT 1")
                .build();
        return new ConnectionPool(connectionPoolConfiguration);
    }

    @Bean
    public R2dbcEntityOperations mcR2dbcTemplate(@Qualifier("mcConnectionFactory") ConnectionFactory connectionFactory) {
        DatabaseClient databaseClient = DatabaseClient.create(connectionFactory);
        log.info("mc DB Connection : {}", connectionFactory.getMetadata().getName());

        return new R2dbcEntityTemplate(databaseClient, MySqlDialect.INSTANCE);
    }
}
