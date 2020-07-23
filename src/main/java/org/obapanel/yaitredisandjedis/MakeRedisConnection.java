package org.obapanel.yaitredisandjedis;

import redis.clients.jedis.Jedis;

import java.util.concurrent.ThreadLocalRandom;

public class MakeRedisConnection {


    public static String HOST = "127.0.0.1";
    public static int PORT = 6379;

    private static Jedis instance;

    public static Jedis jedisNow() {
        if (instance == null) {
            createInstance();
        }
        return instance;
    }

    public static Jedis jedisNew() {
        return new Jedis(HOST, PORT);
    }

    private synchronized static void createInstance() {
        if (instance == null) {
            instance = new Jedis(HOST, PORT);
        }
    }


    public static String createNewValue(){
        return "VALUE_" + ThreadLocalRandom.current().nextInt();
    }
}


