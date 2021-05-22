package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.Semaphore;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNew;

/**
 * Redis Pub/Sub
 */
public class Slide17 {


    /**
     * Commands for subscribe
     * SUBSCRIBE / PUBLISH / UNSUSBSCRIBE
     */
    public void subscribeAndSend() throws InterruptedException {
        Semaphore semaphoreExit = new Semaphore(0);
        Semaphore semaphoreSend = new Semaphore(0);
        Thread tsubs = new Thread(() -> {
            Jedis jedisSub = jedisNew();
            JedisPubSub jedisPubSub = new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    super.onMessage(channel, message);
                    System.out.println("Channel " + channel + " message " + message);
                    semaphoreExit.release();
                    this.unsubscribe(channel);
                }
                @Override
                public void onSubscribe(String channel, int subscribedChannels) {
                    System.out.println("Client is Subscribed to channel : "+ channel + "(" + subscribedChannels + ")");
                    semaphoreSend.release();
                }

                @Override
                public void onUnsubscribe(String channel, int subscribedChannels) {
                    System.out.println("Client is Unsubscribed from channel : "+ channel + "(" + subscribedChannels + ")");
                }
            };
            jedisSub.subscribe(jedisPubSub, "CHANNEL:SLIDE16"); // Here the thread is being bloqued
        });
        tsubs.start();
        semaphoreSend.acquire();
        Jedis jedisPub = jedisNew();
        jedisPub.publish("CHANNEL:SLIDE16", "Hello World");
        System.out.println("Message sent");
        semaphoreExit.acquire();
        tsubs.join();
    }


    public static void main(String[] args) {
        try {
            new Slide17().subscribeAndSend();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}