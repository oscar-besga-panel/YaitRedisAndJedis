package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;


import java.io.UnsupportedEncodingException;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;
import static org.obapanel.yaitredisandjedis.MakeRedisConnection.createNewValue;

/**
 * Redis data
 */
public class Slide78 {


    /**
     * Command in redis
     * EXPIRE / PEXPIRE
     */
    public void setExpireTime() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:TEST:1", createNewValue());
        jedis.expire("KEY:TEST:1", 3); // 3 seconds
        jedis.pexpire("KEY:TEST:1", 3000); // 3000 miliseconds
        assert 3000 > jedis.pttl("KEY:TEST:1");
        assert 3 > jedis.ttl("KEY:TEST:1");
    }


    /**
     * Command in redis
     * EXPIRE / PERSIST
     */
    public void setExpireTimeAndRemoveIt() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:TEST:2", createNewValue());
        jedis.expire("KEY:TEST:2", 3);
        jedis.persist("KEY:TEST:2");
    }


    /**
     * Command in redis
     * EXISTS / DEL / UNLINK
     */
    public void checkDataAndRemoveIt() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:TEST:2", createNewValue());
        boolean exists = jedis.exists("KEY:TEST:2");
        jedis.del("KEY:TEST:2");
        jedis.unlink("KEY:TEST:2");
    }

    /**
     * String or bytes
     */
    public void stringOrBytes() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:TEST:3", createNewValue());
        byte[] bkey = new byte[]{75,69,89,58,84,69,83,84,58,52}; // "KEY:TEST:4"
        byte[] bvalue = new byte[]{72,101,108,108,111,32,87,111,114,108,100}; // "Hello World"
        jedis.set(bkey,bvalue);
        assert jedis.exists("KEY:TEST:3");
        assert jedis.exists(bkey);
        assert "Hello World".equals(jedis.get("KEY:TEST:4"));
    }

    public static void main(String[] args) throws Exception {
//        byte[] b = "Hello World".getBytes("UTF-8");
//        StringBuilder sb = new StringBuilder();
//        for(int i=0; i < b.length; i++){
//            sb.append(b[i]).append(",");
//        }
//
//        System.out.println("Hello World " + sb.toString());
//        new Slide78().stringOrBytes();
    }

}
