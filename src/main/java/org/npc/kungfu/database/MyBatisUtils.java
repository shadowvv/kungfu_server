package org.npc.kungfu.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtils {
    private static SqlSessionFactory sqlSessionFactory;

    static {
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

    public static void insertPlayerInfo(PlayerInfoEntity entity) {
        try (SqlSession session = MyBatisUtils.getSession()) {
            PlayerInfoMapper userMapper = session.getMapper(PlayerInfoMapper.class);
            userMapper.insertPlayerInfo(entity.getId(), entity.getUserName(), entity.getPassword(), entity.getNickName(),
                    entity.getBattleCount(), entity.getWinCount(), entity.getWeaponUseCount(), entity.getWeaponWinCount());
            session.commit();
        }
    }

    public static void main(String[] args) {
        try (SqlSession session = MyBatisUtils.getSession()) {
            PlayerInfoMapper userMapper = session.getMapper(PlayerInfoMapper.class);
            PlayerInfoEntity entity = userMapper.getPlayerInfo("admin", "123456");
            if (entity != null) {
                System.out.println("查询到的用户列表：" + entity);
            } else {
                System.out.println("查询不到用户");
            }
        }
    }
}

