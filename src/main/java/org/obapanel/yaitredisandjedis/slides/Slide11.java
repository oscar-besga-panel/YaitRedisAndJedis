package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis STRING
 */
public class Slide11 {



    /**
     * Command in redis
     * GET / SET / SETNX
     */
    public void getAndSetString() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:STRING:1","VALUE1");
        assert "VALUE1".equals(jedis.get("KEY1"));
        jedis.setnx("KEY:STRING:1","VALUE11");  // SETNX prevents this value to be written
        assert "VALUE1".equals(jedis.get("KEY1"));
    }

    /**
     * Command in redis
     * APPEND / SETRANGE / GETRANGE
     */
    public void operateOnStringAsChars() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:STRING:2","Hello ");
        jedis.append("KEY:STRING:2", "World");
        assert "Hello World".equals(jedis.get("KEY:STRING:2"));
        jedis.setrange("KEY:STRING:2",6,"Redis");
        assert "Hello Redis".equals(jedis.get("KEY:STRING:2"));
        assert "Redis".equals(jedis.getrange("KEY:STRING:2",6,-1));
    }

    /**
     * Command in redis
     * INCR / INCRBY / DECR / DECRBY
     */
    public void operateOnStringAsNumbers() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:STRING:3","0");
        jedis.incr("KEY:STRING:3");
        assert "1".equals(jedis.get("KEY:STRING:3"));
        jedis.incrBy("KEY:STRING:3", 2);
        assert "3".equals(jedis.get("KEY:STRING:3"));
        jedis.decr("KEY:STRING:3");
        assert "2".equals(jedis.get("KEY:STRING:3"));
        jedis.decrBy("KEY:STRING:3", 2);
        assert "0".equals(jedis.get("KEY:STRING:3"));
    }

    /**
     * Command in redis
     * SETBIT / GETBIT / BITCOUNT / BITPOS / BITOP
     */
    public void operateOnStringAsBitmap() {
        Jedis jedis = jedisNow();
        jedis.setbit("KEY:STRING:4",10,true);
        assert true == jedis.getbit("KEY:STRING:4", 10);
        jedis.bitop(BitOP.AND, "KEY:STRING:4", "KEY2");
    }


//    public static void main(String[] args) {

//    }

}
