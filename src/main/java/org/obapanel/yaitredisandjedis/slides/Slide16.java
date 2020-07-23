package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.concurrent.Semaphore;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.createNewValue;
import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNew;
import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis Pub/Sub
 */
public class Slide16 {


    /**
     * Commands for subscribe
     * SUBSCRIBE / PUBLISH / UNSUSBSCRIBE
     */
    public void subscribeAndSend() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Thread tsubs = new Thread(() -> {
            Jedis jedisSub = jedisNew();
            JedisPubSub jedisPubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    super.onMessage(channel, message);
                    System.out.println("Channel " + channel + " message " + message);
                    semaphore.release();
                    this.unsubscribe(channel);
                }
                @Override
                public void onSubscribe(String channel, int subscribedChannels) {
                    System.out.println("Client is Subscribed to channel : "+ channel + "(" + subscribedChannels + ")");
                }

                @Override
                public void onUnsubscribe(String channel, int subscribedChannels) {
                    System.out.println("Client is Unsubscribed from channel : "+ channel + "(" + subscribedChannels + ")");
                }
            };
            jedisSub.subscribe(jedisPubSub, "CHANNEL:SLIDE16");
        });
        tsubs.start();
        Jedis jedisPub = jedisNew();
        jedisPub.publish("CHANNEL:SLIDE16", "Hello World");
        semaphore.acquire();
        tsubs.join();
    }


    public static void main(String[] args) {
        try {
            new Slide16().subscribeAndSend();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}