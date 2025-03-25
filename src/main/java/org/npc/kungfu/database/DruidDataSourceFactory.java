package org.npc.kungfu.database;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DruidDataSourceFactory {
    private static final DruidDataSource dataSource = new DruidDataSource();
    private static final Logger logger = LoggerFactory.getLogger(DruidDataSourceFactory.class);


    public static void init() {
        initialize();
    }

    private static void initialize() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/druid.properties"));

            dataSource.setDriverClassName(properties.getProperty("jdbc.driver"));
            dataSource.setUrl(properties.getProperty("jdbc.url"));
            dataSource.setUsername(properties.getProperty("jdbc.username"));
            dataSource.setPassword(properties.getProperty("jdbc.password"));

            dataSource.setInitialSize(Integer.parseInt(properties.getProperty("druid.initialSize")));
            dataSource.setMinIdle(Integer.parseInt(properties.getProperty("druid.minIdle")));
            dataSource.setMaxActive(Integer.parseInt(properties.getProperty("druid.maxActive")));
            dataSource.setMaxWait(Long.parseLong(properties.getProperty("druid.maxWait")));
            dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(properties.getProperty("druid.timeBetweenEvictionRunsMillis")));
            dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(properties.getProperty("druid.minEvictableIdleTimeMillis")));
            dataSource.setValidationQuery(properties.getProperty("druid.validationQuery"));
            dataSource.setTestWhileIdle(Boolean.parseBoolean(properties.getProperty("druid.testWhileIdle")));
            dataSource.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty("druid.testOnBorrow")));
            dataSource.setTestOnReturn(Boolean.parseBoolean(properties.getProperty("druid.testOnReturn")));
            dataSource.setPoolPreparedStatements(Boolean.parseBoolean(properties.getProperty("druid.poolPreparedStatements")));
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(properties.getProperty("druid.maxPoolPreparedStatementPerConnectionSize")));

            // 打印数据库连接池关键配置信息
            logger.info("Druid DataSource Configuration:");
            logger.info("Driver Class Name: {}", dataSource.getDriverClassName());
            logger.info("URL: {}", dataSource.getUrl());
            logger.info("Username: {}", dataSource.getUsername());
            logger.info("Initial Size: {}", dataSource.getInitialSize());
            logger.info("Min Idle: {}", dataSource.getMinIdle());
            logger.info("Max Active: {}", dataSource.getMaxActive());
            logger.info("Max Wait: {}", dataSource.getMaxWait());
            logger.info("Time Between Eviction Runs Millis: {}", dataSource.getTimeBetweenEvictionRunsMillis());
            logger.info("Min Evictable Idle Time Millis: {}", dataSource.getMinEvictableIdleTimeMillis());
            logger.info("Validation Query: {}", dataSource.getValidationQuery());
            logger.info("Test While Idle: {}", dataSource.isTestWhileIdle());
            logger.info("Test On Borrow: {}", dataSource.isTestOnBorrow());
            logger.info("Test On Return: {}", dataSource.isTestOnReturn());
            logger.info("Pool Prepared Statements: {}", dataSource.isPoolPreparedStatements());
            logger.info("Max Pool Prepared Statements Per Connection Size: {}", dataSource.getMaxPoolPreparedStatementPerConnectionSize());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("加载 Druid 配置失败", e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
