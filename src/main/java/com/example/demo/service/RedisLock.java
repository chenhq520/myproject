package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;

@Service
public class RedisLock {

    /**
     * redis的锁键
     */
    private String lock_key = "lock";

    private long lockTime = 30;

    private long timeout = 60_000;

    SetParams params = SetParams.setParams().nx().px(lockTime);

    @Autowired
    private JedisPool jedisPool;

    public boolean lock(String id) {
        Jedis jedis = jedisPool.getResource();
        long startTime = System.currentTimeMillis();
        try {
            while (true) {
                String lock = jedis.set(lock_key, id, params);
                if ("OK".equals(lock)) {
                    return true;
                }

                long l = System.currentTimeMillis() - startTime;
                if (l >= timeout) {
                    return false;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            jedis.close();
        }
    }

    public boolean unlock(String id) {
        Jedis jedis = jedisPool.getResource();
        String script =
                "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                        "   return redis.call('del',KEYS[1]) " +
                        "else" +
                        "   return 0 " +
                        "end";
        try {
            Object result = jedis.eval(script, Collections.singletonList(lock_key), Collections.singletonList(id));
            if ("1".equals(result.toString())) {
                return true;
            }
            return false;
        } finally {
            jedis.close();
        }
    }

}
