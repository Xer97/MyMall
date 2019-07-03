package com.xer97.mymall.common;

import com.xer97.mymall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author xer97
 * @date 2019/6/16 9:04
 */
public class RedisPool {
    /**
     * jedis连接池
     */
    private static JedisPool pool;

    private static String redisIp = PropertiesUtil.getProperty("redis.ip");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port"));
    /**
     * 最大连接数
     */
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
    /**
     * 最大空闲连接数
     */
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));
    /**
     * 最小空闲连接数
     */
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));
    /**
     * 向外提供连接对象时判断该连接是否可用
     */
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));
    /**
     * 回收连接对象是判断该连接是否可用
     */
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));
    ;

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        // 默认为true，连接耗尽时，是否阻塞，false会抛异常，true会阻塞到超时
        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort, 1000 * 2);
    }

    static {
        initPool();
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

//    public static void main(String[] args) {
//        Jedis jedis = getJedis();
//        jedis.set("xer97k","xer97v");
//        returnResource(jedis);
//        pool.destroy();
//        System.out.println("program is end");
//    }


}
