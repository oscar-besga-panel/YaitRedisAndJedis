package org.obapanel.yaitredisandjedis.slides;

import com.sun.source.tree.LabeledStatementTree;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNew;
import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Distribute tasks
 */
public class Slide19 {

    public static final String LIST_NAME = "KEY:DISTR_TASKS:1";


    public void setTasks() {
        Jedis jedis  = jedisNow();
        jedis.rpush(LIST_NAME, "TASK_1:order:names");
    }


    public void recoverTasks() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i =0 ; i < 5; i++) {
            executorService.submit(() -> {
                Jedis jedis  = jedisNew();
                List<String> tasks = jedis.blpop(LIST_NAME);
                String task = tasks.get(0);
                System.out.println("Executing task " + task);
            });
        }
    }
}
