package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.List;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.createRandomNewValue;
import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis OrderedSET
 */
public class Slide16 {


    /**
     * Commands pipeline in redis
     */
    public void pipeLineOperation() {
        Jedis jedis = jedisNow();
        Pipeline pipeline = jedis.pipelined();
        for(int i= 0; i < 100; i++){
            pipeline.set("KEY:PIPE:" + i, createRandomNewValue());
        }
        Response<List<Object>> results = pipeline.exec();
    }


    /**
     * Commands transaction in redis
     * MULTI / EXEC
     */
    public void transactionOperation(boolean make3Operations) {
        Jedis jedis = jedisNow();
        Transaction transaction = jedis.multi();
        transaction.set("KEY:TRANS:1","12");
        transaction.setnx("KEY:TRANS:2","22");
        if (make3Operations) {
            transaction.set("KEY:TRANS:3","13");
        }
        Response<String> result = transaction.get("KEY:TRANS:2");
        // NOOOOO
//        if (result.get() == null) {
//            transaction.set("KEY:TRANS:2","2");
//        }
        transaction.exec();
        assert "22".equals(result.get());
    }



    public static void main(String[] args) {
        new Slide16().transactionOperation(true);
        new Slide16().transactionOperation(false);
    }


}