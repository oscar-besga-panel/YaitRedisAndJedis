@Grab(group='redis.clients', module='jedis', version='2.10.2')
import redis.clients.jedis.*;

hostname = "127.0.0.1"
port=6379
password=""

Jedis jedis = new Jedis(hostname, port)
if (password != null && password.size() > 0) {
    jedis.auth(password);
}
jedis.set("KEY1","Hello ")
jedis.append("KEY1", "World")
result = jedis.get("KEY1")
println "Result from redis is ${result}"