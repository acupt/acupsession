package com.acupt.acupsession;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

/**
 * Created by liujie on 2018/5/17.
 */
public class SessionStore {

    private static final String confPrefix = "acupsession_conf";
    private static final String attrPrefix = "acupsession_attr";
    private static final int expire = 60 * 60 * 24;

    private JedisPool jedisPool;

    public SessionStore(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setAttribute(String sessionId, String name, Object value) {
        set(connect(attrPrefix, sessionId, name), value, expire);
    }

    public Object getAttribute(String sessionId, String name) {
        return get(connect(attrPrefix, sessionId, name));
    }

    public void touch(String sessionId) {
        set(connect(confPrefix, sessionId), System.currentTimeMillis(), expire);
    }

    public String create() {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        touch(id);
        return id;
    }

    private String connect(String... ss) {
        StringBuilder sb = new StringBuilder();
        for (String s : ss) {
            if (sb.length() > 0) {
                sb.append(":");
            }
            sb.append(s);
        }
        return sb.toString();
    }

    private void set(String key, Object value, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            byte[] bytes = SerialUtil.objectToByte(value);
            jedis.set(key.getBytes(), bytes);
            jedis.expire(key.getBytes(), expire);
        } finally {
            jedis.close();
        }
    }

    private Object get(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            byte[] bytes = jedis.get(key.getBytes());
            return SerialUtil.byteToObject(bytes);
        } finally {
            jedis.close();
        }
    }
}
