package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.createRandomNewValue;
import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis STRING
 */
public class Slide10 {



    /**
     * Command in redis
     * SCAN
     */
    public void scan() {
        Jedis jedis = jedisNow();
        // Given
        for(int i=0; i < 100; i++){
            if (i % 2 == 0) {
                jedis.set("KEY:SCAN:EVEN:" + i, createRandomNewValue());
            } else {
                jedis.set("KEY:SCAN:ODD:" + i, createRandomNewValue());
            }
        }

        // Then
        List<String> listOfKeys = new ArrayList<>();
        ScanParams scanParams = new ScanParams().count(2).match("KEY:SCAN:EVEN:*"); // Scan on two-by-two responses
        String cursor = ScanParams.SCAN_POINTER_START;
        boolean cycleIsFinished = false;
        while(!cycleIsFinished) {
            ScanResult<String> partialResult =  jedis.scan(cursor, scanParams);
            cursor = partialResult.getCursor();
            listOfKeys.addAll(partialResult.getResult());
            cycleIsFinished = cursor.equals(ScanParams.SCAN_POINTER_START);
        }
        //Expect
        assert 50 == listOfKeys.size();
        assert listOfKeys.contains("KEY:SCAN:EVEN:0");
    }



    public static void main(String[] args) {
        new Slide10().scan();
    }

}
