import redis.clients.jedis.*;


class ExampleGroovyClass {


    static void main(String[] args) {
        def hostname = "127.0.0.1"
        def port=6379
        def password=""

        Jedis jedis = new Jedis(hostname, port)
        if (password != null && password.size() > 0){
            jedis.auth(password);
        }
        jedis.set("KEY1","Hello ")
        jedis.append("KEY1", "World")
        def result = jedis.get("KEY1")
        println "Result from redis is ${result}"
    }
}

