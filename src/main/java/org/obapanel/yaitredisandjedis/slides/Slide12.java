package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNew;
import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis LIST
 */
public class Slide12 {

    /**
     * Command in redis
     * LPUSH / RPUSH / LRANGE / LTRIM / LPOP / RPOP
     */
    public void listOperations() {
        Jedis jedis = jedisNow();
        jedis.lpush("KEY:LIST:1","Hello");
        jedis.rpush("KEY:LIST:1", "World");
        List<String> list = jedis.lrange("KEY1L",0,-1);
        assert list.containsAll(Arrays.asList("Hello", "World"));
        jedis.ltrim("KEY:LIST:1", 0, 1);
        assert !list.containsAll(Arrays.asList("Hello", "World"));
        assert "Hello".equals( jedis.lpop("KEY1L"));
        assert "World".equals( jedis.rpop("KEY1L"));
    }


    /**
     * Command in redis
     * LPUSH / BLPOP
     *
     * Caution: for blocking operations, the connection is being bloqued and
     * can not be used for other operatiosn. Use a pool or another new connections
     */
    public void listOperationsBlocking () {
        final Semaphore semaphore = new Semaphore(0);
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        Runnable getter = () -> {
            Jedis jedis1 = jedisNew();
            List<String> result = jedis1.blpop(0,"KEY:LIST:2");
            System.out.println("blpop result"  + result);
            if (result.get(0).equals("KEY:LIST:2") && result.get(1).equals("Hello"))  {
                semaphore.release();
            }
        };
        Runnable setter = () -> {
            Jedis jedis2 = jedisNew();
            jedis2.lpush("KEY:LIST:2","Hello");
        };
        executorService.schedule(getter, 1L, TimeUnit.SECONDS);
        executorService.schedule(setter, 3L, TimeUnit.SECONDS);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

//    public static void main(String[] args) {
//        new Slide11().listOperationsBloqucking();
//    }

}
