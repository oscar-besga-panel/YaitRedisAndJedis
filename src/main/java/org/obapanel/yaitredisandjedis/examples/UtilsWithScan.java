package org.obapanel.yaitredisandjedis.examples;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Class with utils with SCAN method
 * Ready to use with your jedis code or to copy where you need
 */
public class UtilsWithScan {


    /**
     * Retrieve keys of redis with a pattern
     * Can be redone to hscan and others scanss with ease
     * @param jedis Jedis conection
     * @param pattern String with pattern to match
     * @return list of keys that matches
     */
    public static List<String> retriveListOfKeys(Jedis jedis , String pattern) {
        List<String> listOfKeys = new ArrayList<>();
        ScanParams scanParams = new ScanParams().match(pattern); // Scan on two-by-two responses
        String cursor = ScanParams.SCAN_POINTER_START;
        do {
            ScanResult<String> partialResult =  jedis.scan(cursor, scanParams);
            cursor = partialResult.getCursor();
            listOfKeys.addAll(partialResult.getResult());
        }  while(!cursor.equals(ScanParams.SCAN_POINTER_START));
        return listOfKeys;
    }


    /**
     * Retrieve keys of redis with a pattern, and their associated values
     * Can be redone to hscan and others scanss with ease
     * @param jedis Jedis conection
     * @param pattern String with pattern to match
     * @return list of keys that matches
     */
    public static Map<String,String> retriveMapOfKeyValues(Jedis jedis , String pattern) {
        Map<String,String> mapOfKeyValues = new HashMap<>();
        ScanParams scanParams = new ScanParams().match(pattern); // Scan on two-by-two responses
        String cursor = ScanParams.SCAN_POINTER_START;
        do {
            ScanResult<String> partialResult =  jedis.scan(cursor, scanParams);
            cursor = partialResult.getCursor();
            partialResult.getResult().forEach( result -> mapOfKeyValues.put(result, jedis.get(result)));
        }  while(!cursor.equals(ScanParams.SCAN_POINTER_START));
        return mapOfKeyValues;
    }

    /**
     * Retrieve keys of redis with a pattern, and their associated values
     * Can be redone to hscan and others scanss with ease
     * This version is more complicated but groups all the get operations into a pipeline,
     *   which greatly increases performance
     * @param jedis Jedis conection
     * @param pattern String with pattern to match
     * @return list of keys that matches
     */
    public static Map<String,String> retriveMapOfKeyValues_version2(Jedis jedis , String pattern) {
        Map<String,Response<String>> mapOfKeyResponses = new HashMap<>();
        ScanParams scanParams = new ScanParams().match(pattern); // Scan on two-by-two responses
        String cursor = ScanParams.SCAN_POINTER_START;
        do {
            ScanResult<String> partialResult =  jedis.scan(cursor, scanParams);
            cursor = partialResult.getCursor();
            partialResult.getResult().forEach( result -> mapOfKeyResponses.put(result, null));
        }  while(!cursor.equals(ScanParams.SCAN_POINTER_START));
        Pipeline pipeline = jedis.pipelined();
        mapOfKeyResponses.keySet().forEach( key -> {
            Response<String> response = pipeline.get(key);
            mapOfKeyResponses.put(key, response);
        });
        pipeline.sync();
        Map<String,String> mapOfKeyValues = new HashMap<>();
        mapOfKeyResponses.entrySet().forEach( entry -> {
            mapOfKeyValues.put(entry.getKey(), entry.getValue().get());
        });

        return mapOfKeyValues;
    }

    /**
     * Retrieve keys of redis with a patter and executes the function for every result
     * Can be redone to hscan and others scanss with ease
     * @param jedis Jedis conection
     * @param pattern String with pattern to match
     * @param onKey What to do with each key retrieved
     */
    public static void executeWithinListOfKeys(Jedis jedis, String pattern, Consumer<String> onKey) {
        ScanParams scanParams = new ScanParams().match(pattern); // Scan on two-by-two responses
        String cursor = ScanParams.SCAN_POINTER_START;
        do {
            ScanResult<String> partialResult =  jedis.scan(cursor, scanParams);
            cursor = partialResult.getCursor();
            partialResult.getResult().forEach(onKey);
        }  while(!cursor.equals(ScanParams.SCAN_POINTER_START));
    }

}
