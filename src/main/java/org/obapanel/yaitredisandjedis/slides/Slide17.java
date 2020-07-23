package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNew;
import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis Scripts
 */
public class Slide17 {

    public static final String SCRIPT = "" +
            "local num= redis.call('get',KEYS[1])  " + "\n" +
            "num = num + ARGV[1]" + "\n" +
            "return num  ";


    /**
     * Commands for scripts
     * EVAL
     */
    public void executeScript() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:SCRIPT:1","1");
        List<String> keys = Arrays.asList("KEY:SCRIPT:1");
        List<String> args = Arrays.asList("2");
        Object result = jedis.eval(SCRIPT, keys, args  );
        assert 3L == (Long)result;
    }


    /**
     * Commands for scripts
     * EVAL
     */
    public void executeLoadedScript() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:SCRIPT:1","1");
        List<String> keys = Arrays.asList("KEY:SCRIPT:1");
        List<String> args = Arrays.asList("2");
        String sha1 = jedis.scriptLoad(SCRIPT);
        Object result = jedis.evalsha(sha1, keys, args  );
        assert 3L == (Long)result;
    }


    public static void main(String[] args) {
        new Slide17().executeScript();
    }


}