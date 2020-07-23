package org.obapanel.yaitredisandjedis.slides;

import org.obapanel.yaitredisandjedis.MakeRedisConnection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;

/**
 * Class of multilevel cache
 */
public class Slide20 {

    String hashInRedis;
    Map<String, String> cache = new HashMap<>();
    Jedis jedis = MakeRedisConnection.jedisNew();

    public String get(String key) {
        String data = cache.get(key);
        if (data == null) {
            data = jedis.hget(hashInRedis, key);
            if (data == null) {
                data = "GET FROM DATABASE";
                jedis.hset(hashInRedis, key, data);
            }
            cache.put(key, data);
        }
        return data;
    }

    public void update(String key, String data) {
        System.out.println("UPDATE DATA IN DATABASE " + key + data);
        cache.put(key, data);
        jedis.hset(hashInRedis, key, data);
        jedis.publish(hashInRedis + ":Channel", key);
    }

    public void subscribeToInvalidate() {
        Jedis jedisChannel = MakeRedisConnection.jedisNew();
        JedisPubSub jedisPubSub = new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (channel.equals(hashInRedis  + ":Channel")) {
                    cache.remove(message);
                }
            }
        };
        jedisChannel.subscribe(jedisPubSub, hashInRedis  + ":Channel");
    }
}
