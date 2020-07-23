package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNew;
import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis HASH
 */
public class Slide12 {




    /**
     * Commands in redis
     * HSET / HGET / HSETNX / HMSET / HMGET
     */
    public void hashOperations(){
        Jedis jedis = jedisNow();
        jedis.hset("KEY:HASH:1","key1","value1");
        assert "value1".equals( jedis.hget("KEY:HASH:1","key1") );
        jedis.hsetnx("KEY:HASH:1","key1","value11");
        assert "value1".equals( jedis.hget("KEY:HASH:1","key1") );
        Map<String, String> data = new HashMap<>();
        data.put("key2", "value2");
        data.put("key3", "value3");
        jedis.hmset("KEY:HASH:1", data);
        List<String> result = jedis.hmget("KEY:HASH:1", "key1", "key2", "key3");
        assert result.containsAll(Arrays.asList("value1", "value2", "value3"));
        assert 3 == jedis.hlen("KEY:HASH:1");
    }

    /**
     * Commands in redis
     * HKEYS / HVALS / HSETNX / HMSET / HMGET
     */
    public void hashKeysAndValues(){
        Jedis jedis = jedisNow();
        jedis.hset("KEY:HASH:2","key1a","value1a");
        jedis.hset("KEY:HASH:2","key1b","value1b");
        jedis.hset("KEY:HASH:2","key1c","value1c");
        assert jedis.hkeys("KEY:HASH:2").containsAll(Arrays.asList("key1a", "key1b", "key1c"));
        assert jedis.hvals("KEY:HASH:2").containsAll(Arrays.asList("value1a", "value1b", "value1c"));

        jedis.hset("KEY:HASH:2","key1d","value1d");
        jedis.hset("KEY:HASH:2","key1e","value1e");
        jedis.hset("KEY:HASH:2","key1f","value1f");
        jedis.hset("KEY:HASH:2","key1g","value1g");
        jedis.hset("KEY:HASH:2","key1h","value1h");
        jedis.hset("KEY:HASH:2","key1i","value1i");
        jedis.hset("KEY:HASH:2","key1j","value1j");

        Map<String, String> scanResult = new HashMap<>();
        ScanParams scanParams = new ScanParams().count(2); // Scan on two-by-two responses
        String cursor = ScanParams.SCAN_POINTER_START;
        boolean cycleIsFinished = false;
        while(!cycleIsFinished) {
            ScanResult<Map.Entry<String, String>> partialResult =  jedis.hscan("KEY:HASH:2", cursor, scanParams);
            cursor = partialResult.getCursor();
            partialResult.getResult().forEach( e -> scanResult.put(e.getKey(), e.getValue()));
            cycleIsFinished = cursor.equals(ScanParams.SCAN_POINTER_START);
        }
        assert "value1h".equals(scanResult.get("key1h"));
        assert "value1h".equals(jedis.hget("KEY:HASH:2", "key1h"));

    }


    public static void main(String[] args) {
        new Slide12().hashKeysAndValues();
    }

}
