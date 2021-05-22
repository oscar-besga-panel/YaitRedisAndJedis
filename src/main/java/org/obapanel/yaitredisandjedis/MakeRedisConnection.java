package org.obapanel.yaitredisandjedis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.ThreadLocalRandom;

public class MakeRedisConnection {


    public static String HOST = "127.0.0.1";
    public static int PORT = 6379;

    private static volatile Jedis instance;

    public static Jedis jedisNow() {
        Jedis connection = instance;
        while (connection == null) {
            createInstance();
            connection = instance;
        }
        return connection;
    }

    public static Jedis jedisNew() {
        return new Jedis(HOST, PORT);
    }

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = jedisNew();
        }
    }


    public static String createRandomNewValue(){
        return "VALUE_" + ThreadLocalRandom.current().nextInt();
    }
}


