package com.acupt.acupsession;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liujie on 2018/5/17.
 */
public class SessionStore {

    private static final String confPrefix = SessionConfig.getRedisPrefix() + "_conf";
    private static final String attrPrefix = SessionConfig.getRedisPrefix() + "_attr";

    private static Map<String, SessionContext> contextMap = new ConcurrentHashMap<String, SessionContext>();

    private JedisPool jedisPool;

    public SessionStore(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setAttribute(String sessionId, String name, Object value) {
        set(attrKey(sessionId, name), value, getExpire(sessionId));
    }

    public Object getAttribute(String sessionId, String name) {
        return get(attrKey(sessionId, name));
    }

    public void removeAttribute(String sessionId, String name) {
        del(attrKey(sessionId, name));
    }

    public Enumeration<String> getAttributeNames(String sessionId) {
        Jedis jedis = jedisPool.getResource();
        try {
            Set<String> set = jedis.keys(attrKey(sessionId, "*"));
            Vector<String> vector = new Vector<String>();
            int i = attrKey(sessionId, "").length();
            for (String key : set) {
                vector.add(key.substring(i));
            }
            return vector.elements();
        } finally {
            SerialUtil.close(jedis);
        }
    }

    public SessionContext create() {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        SessionContext context = new SessionContext(id);
        set(confKey(id), context, context.getMaxInactiveInterval());
        addAlive(id);
        contextMap.put(context.getId(), context);
        return context;
    }

    public void touch(String sessionId) {
        expire(confKey(confPrefix, sessionId), getExpire(sessionId));
    }

    public void invalidate(String id) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.srem(confKey("alive"), id);
            Set<String> set = jedis.keys(confKey(id));
            set.addAll(jedis.keys(attrKey(id)));
            String[] keys = new String[set.size()];
            set.toArray(keys);
            jedis.del(keys);
        } finally {
            SerialUtil.close(jedis);
        }
    }

    public SessionContext find(String id) {
        if (!isAlive(id)) {
            return null;
        }
        SessionContext context = (SessionContext) get(confKey(id));
        if (context != null) {
            contextMap.put(context.getId(), context);
        }
        return context;
    }

    public void save(String id) {
        SessionContext context = getSessionContext(id);
        set(confKey(id), context, context.getMaxInactiveInterval());
    }

    public boolean isAlive(String id) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.sismember(confKey("alive"), id);
        } finally {
            SerialUtil.close(jedis);
        }
    }

    public long addAlive(String ids) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.sadd(confKey("alive"), ids);
        } finally {
            SerialUtil.close(jedis);
        }
    }

    private SessionContext getSessionContext(String sessionId) {
        return contextMap.get(sessionId);//请求刚进来就放进来了，不存在找不到的情况
    }

    private int getExpire(String sessionId) {
        SessionContext context = getSessionContext(sessionId);
        if (context != null) {
            context.getMaxInactiveInterval();
        }
        return SessionConfig.getSessionMaxInactiveInterval();
    }

    private void set(String key, Object value, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            byte[] bytes = SerialUtil.objectToByte(value);
            jedis.set(key.getBytes(), bytes);
            jedis.expire(key.getBytes(), expire);
        } finally {
            SerialUtil.close(jedis);
        }
    }

    private Object get(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            byte[] bytes = jedis.get(key.getBytes());
            return SerialUtil.byteToObject(bytes);
        } finally {
            SerialUtil.close(jedis);
        }
    }

    private Long expire(String key, int expire) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.expire(key, expire);
        } finally {
            SerialUtil.close(jedis);
        }
    }

    private Long del(String... keys) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.del(keys);
        } finally {
            SerialUtil.close(jedis);
        }
    }

    private String attrKey(String... ss) {
        return connect1(attrPrefix, ss);
    }

    private String confKey(String... ss) {
        return connect1(confPrefix, ss);
    }

    private String connect1(String prefix, String... ss) {
        StringBuilder sb = new StringBuilder(prefix);
        for (String s : ss) {
            if (sb.length() > 0) {
                sb.append(":");
            }
            sb.append(s);
        }
        return sb.toString();
    }
}
