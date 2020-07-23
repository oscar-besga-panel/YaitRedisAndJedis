package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import java.util.HashSet;
import java.util.Set;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis OrderedSET
 */
public class Slide14 {


    /**
     * Commands in redis
     * ZADD / ZPOPMAX / ZPOPMIN
     */
    public void zsetOperations() {
        Jedis jedis = jedisNow();
        jedis.zadd("KEY:ZSET:1",3.0, "VALUE1");
        jedis.zadd("KEY:ZSET:1", 2.0, "VALUE2");
        jedis.zadd("KEY:ZSET:1", 1.0, "VALUE3"); // This will no add
        assert 3 == jedis.zcount("KEY:ZSET:1","-inf", "+inf");
        /*
        DESDE 5.0
         */
//        Set<Tuple> popMax = jedis.zpopmax("KEY:ZSET:1",1);
//        Set<Tuple> popMin = jedis.zpopmin("KEY:ZSET:1",1);
//        assert "VALUE1".equals(popMax.iterator().next().getElement());
//        assert "VALUE3".equals(popMin.iterator().next().getElement());
        Set<String> result = jedis.zrangeByScore("KEY:ZSET:1", 1.5, 2.5 );
        assert "VALUE1".equals(result.iterator().next()) && result.size() == 1;
    }




    public static void main(String[] args) {
        new Slide14().zsetOperations();
    }


}