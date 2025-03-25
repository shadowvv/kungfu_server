package org.npc.kungfu.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtils {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisUtils.class);
    private static SqlSessionFactory sqlSessionFactory;


    public static void init() {
        initialize();
    }

    private static void initialize() {
        try {
            // 读取 MyBatis 配置文件
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

            // 手动设置 Druid 数据源
            DataSource dataSource = DruidDataSourceFactory.getDataSource();
            sqlSessionFactory.getConfiguration().setEnvironment(
                    new org.apache.ibatis.mapping.Environment("development",
                            new org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory(),
                            dataSource));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("初始化 MyBatis 失败", e);
        }
    }

    public static SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }

    public static Boolean insertPlayerInfo(PlayerInfoEntity entity) {
        try (SqlSession session = MyBatisUtils.getSession()) {
            PlayerInfoMapper userMapper = session.getMapper(PlayerInfoMapper.class);
            userMapper.insertPlayerInfo(entity.getId(), entity.getUserName(), entity.getPassword(), entity.getNickName(),
                    entity.getBattleCount(), entity.getWinCount(), entity.getWeaponUseCount(), entity.getWeaponWinCount());
            session.commit();
            return true;
        } catch (Exception e) {
            logger.error("Failed to insert player info: {}", e.getMessage());
            return false;
        }
    }

    public static PlayerInfoEntity getPlayerInfo(String userName, String password) {
        try (SqlSession session = MyBatisUtils.getSession()) {
            PlayerInfoMapper userMapper = session.getMapper(PlayerInfoMapper.class);
            return userMapper.getPlayerInfo(userName, password);
        } catch (Exception e) {
            logger.error("Failed to get player info: {}", e.getMessage());
            return null;
        }
    }
}
