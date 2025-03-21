package org.npc.kungfu.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisUtil {
    private static final JedisPool jedisPool;

    static {
        // Jedis 连接池配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(50); // 最大连接数
        poolConfig.setMaxIdle(10);  // 最大空闲连接数
        poolConfig.setMinIdle(5);   // 最小空闲连接数
        poolConfig.setTestOnBorrow(true); // 获取连接时检测可用性
        poolConfig.setTestOnReturn(true); // 归还连接时检测可用性
        poolConfig.setBlockWhenExhausted(true); // 连接耗尽时是否阻塞等待

        // 初始化连接池（单机模式）
        jedisPool = new JedisPool(poolConfig, "localhost", 6379);
    }

    /**
     * 获取 Jedis 连接（推荐使用 try-with-resources 自动释放）
     */
    private static Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 检查 Redis 连接是否正常
     *
     * @return 连接是否正常
     */
    public static boolean isConnectionOk() {
        try (Jedis jedis = getJedis()) {
            return jedis.ping().equals("PONG");
        } catch (JedisConnectionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ========== String 操作 ==========
     */

    public static void set(String key, String value) {
        try (Jedis jedis = getJedis()) {
            jedis.set(key, value);
        }
    }

    public static String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        }
    }

    public static void setex(String key, String value, int seconds) {
        try (Jedis jedis = getJedis()) {
            jedis.setex(key, seconds, value);
        }
    }

    public static void del(String key) {
        try (Jedis jedis = getJedis()) {
            jedis.del(key);
        }
    }

    /**
     * ========== Hash 操作 ==========
     */

    public static void hset(String key, String field, String value) {
        try (Jedis jedis = getJedis()) {
            jedis.hset(key, field, value);
        }
    }

    public static String hget(String key, String field) {
        try (Jedis jedis = getJedis()) {
            return jedis.hget(key, field);
        }
    }

    public static Map<String, String> hgetAll(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.hgetAll(key);
        }
    }

    /**
     * ========== List 操作 ==========
     */

    public static void lpush(String key, String... values) {
        try (Jedis jedis = getJedis()) {
            jedis.lpush(key, values);
        }
    }

    public static List<String> lrange(String key, int start, int end) {
        try (Jedis jedis = getJedis()) {
            return jedis.lrange(key, start, end);
        }
    }

    /**
     * ========== Set 操作 ==========
     */

    public static void sadd(String key, String... values) {
        try (Jedis jedis = getJedis()) {
            jedis.sadd(key, values);
        }
    }

    public static Set<String> smembers(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.smembers(key);
        }
    }

    /**
     * ========== SortedSet 操作 ==========
     */

    public static void zadd(String key, double score, String member) {
        try (Jedis jedis = getJedis()) {
            jedis.zadd(key, score, member);
        }
    }

    public static List<String> zrange(String key, int start, int end) {
        try (Jedis jedis = getJedis()) {
            return jedis.zrange(key, start, end);
        }
    }

    /**
     * ========== 过期时间 ==========
     */

    public static void expire(String key, int seconds) {
        try (Jedis jedis = getJedis()) {
            jedis.expire(key, seconds);
        }
    }

    public static long ttl(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.ttl(key);
        }
    }

    /**
     * ========== 关闭连接池（程序退出时调用） ==========
     */
    public static void closePool() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    public static void main(String[] args) {
        // 设置 Key
        JedisUtil.set("user:1001", "npc_kungfu");
        System.out.println("获取 user:1001 -> " + JedisUtil.get("user:1001"));

        // 操作 Hash
        JedisUtil.hset("player:2001", "name", "Warrior");
        System.out.println("获取 player:2001 name -> " + JedisUtil.hget("player:2001", "name"));

        // 处理 List
        JedisUtil.lpush("queue", "task1", "task2", "task3");
        System.out.println("任务队列: " + JedisUtil.lrange("queue", 0, -1));

        // 过期时间
        JedisUtil.expire("user:1001", 10);
        System.out.println("TTL user:1001 -> " + JedisUtil.ttl("user:1001"));

        // 关闭连接池（应用退出时）
        JedisUtil.closePool();
    }
}

