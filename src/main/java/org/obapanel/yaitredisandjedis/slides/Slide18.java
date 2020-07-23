package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;

import java.util.concurrent.ThreadLocalRandom;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Locks
 */
public class Slide18 {

    public static final String LOCK_NAME = "KEY:LOCK:1";


    public void lock() {
        Jedis jedis  = jedisNow();
        boolean lockObtained = false;
        String lockValue = ThreadLocalRandom.current().nextLong() + "_" + System.currentTimeMillis();
        synchronized (this) {
            while (!lockObtained) {
                jedis.setnx(LOCK_NAME, lockValue);
                String actualLock = jedis.get(LOCK_NAME);
                lockObtained = lockValue.equals(actualLock);
            }
        }
        // DO lock instructions
        jedis.del(LOCK_NAME);

    }
}
