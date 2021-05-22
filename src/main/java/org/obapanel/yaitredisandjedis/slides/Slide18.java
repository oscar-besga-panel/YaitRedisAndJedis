package org.obapanel.yaitredisandjedis.slides;

import redis.clients.jedis.Jedis;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static org.obapanel.yaitredisandjedis.MakeRedisConnection.jedisNow;

/**
 * Redis Scripts
 */
public class Slide18 {

    public static final String SCRIPT = "" +
            "redis.call('ECHO', '_LOGDEBUG KEYS[1] ' .. KEYS[1] .. ' ARGV[1] ' .. ARGV[1]);" + "\n" +
            "local snum= redis.call('get',KEYS[1])  " + "\n" +
            "local num = tonumber(snum) " + "\n" +
            "if num < 0 then" + "\n" +
            "  return nil" + "\n" +
            "elseif num > 0  then " + "\n" +
            "  num = num + ARGV[1]" + "\n" +
            "end " + "\n" +
            "return num ";


    /**
     * Commands for scripts
     * EVAL
     */
    public void executeScript() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:SCRIPT:1","1");
        List<String> keys = Arrays.asList("KEY:SCRIPT:1");
        List<String> args = Arrays.asList("2");
        Object result = jedis.eval(SCRIPT, keys, args  );
        assert 3L == (Long)result;
        System.out.println("Result " + result);
    }


    /**
     * Commands for scripts
     * SCRIPT LOAD / EVALSHA
     */
    public void executeLoadedScript() {
        Jedis jedis = jedisNow();
        jedis.set("KEY:SCRIPT:1","1");
        List<String> keys = Arrays.asList("KEY:SCRIPT:1");
        List<String> args = Arrays.asList("2");
        String sha1 = jedis.scriptLoad(SCRIPT);
        System.out.println("REDIS SHA1 " + sha1);
        System.out.println("JDK   SHA1 " + sha1FromJDK(SCRIPT) + "\n");
        Object result = jedis.evalsha(sha1, keys, args  );
        assert 3L == (Long)result;
        System.out.println("Result " + result);
    }


    public static String sha1FromJDK(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(text.getBytes("utf8"));
            return String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void main(String[] args) {
        new Slide18().executeLoadedScript();
    }


}