package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis SET
 */
public class Slide14 {


    /**
     * Commands in redis
     * SADD / SCARD / SPOP
     */
    public void setOperations() {
        Jedis jedis = jedisNow();
        jedis.sadd("KEY:SET:1","VALUE1");
        jedis.sadd("KEY:SET:1","VALUE2");
        jedis.sadd("KEY:SET:1","VALUE1"); // This will no add
        assert 2 == jedis.scard("KEY:SET:1");
        String pop = jedis.spop("KEY:SET:1");
        assert "VALUE1".equals(pop) || "VALUE2".equals(pop);
    }


    /**
     * Commands in redis
     * SMEMBERS / SSCAN
     */
    public void setScanOperations() {
        Jedis jedis = jedisNow();
        jedis.sadd("KEY:SET:1","VALUE1");
        jedis.sadd("KEY:SET:1","VALUE2");
        jedis.sadd("KEY:SET:1","VALUE3");
        jedis.sadd("KEY:SET:1","VALUE4");
        jedis.sadd("KEY:SET:1","VALUE5");
        assert jedis.smembers("KEY:SET:1").contains("VALUE4");

        Set<String> scanResult = new HashSet<>();
        ScanParams scanParams = new ScanParams().count(2); // Scan on two-by-two responses
        String cursor = ScanParams.SCAN_POINTER_START;
        boolean cycleIsFinished = false;
        while(!cycleIsFinished) {
            ScanResult<String> partialResult =  jedis.sscan("KEY:SET:1", cursor, scanParams);
            cursor = partialResult.getCursor();
            partialResult.getResult().forEach( e -> scanResult.add(e));
            cycleIsFinished = cursor.equals(ScanParams.SCAN_POINTER_START);
        }
        assert scanResult.contains("VALUE4");
    }


}