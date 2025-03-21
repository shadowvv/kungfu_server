package org.npc.kungfu.database;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DruidDataSourceFactory {
    private static final DruidDataSource dataSource = new DruidDataSource();

    static {
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

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("加载 Druid 配置失败", e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
