package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.createNewValue;
import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis OrderedSET
 */
public class Slide15 {


    /**
     * Commands pipeline in redis
     */
    public void pipeLineOperation() {
        Jedis jedis = jedisNow();
        Pipeline pipeline = jedis.pipelined();
        for(int i= 0; i < 100; i++){
            pipeline.set("KEY:PIPE:" + i, createNewValue());
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
        transaction.exec();
        assert "22".equals(result.get());
    }



    public static void main(String[] args) {
        new Slide15().transactionOperation(true);
        new Slide15().transactionOperation(false);
    }


}