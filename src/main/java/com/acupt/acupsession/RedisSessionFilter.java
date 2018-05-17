package com.acupt.acupsession;

import org.springframework.web.filter.OncePerRequestFilter;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liujie on 2018/5/8.
 */
public class RedisSessionFilter extends OncePerRequestFilter {

    private String host = "localhost";
    private int port = 6379;
    private String password = "000000";
    private int timeout = Protocol.DEFAULT_TIMEOUT;
    private SessionStore sessionStore;

    public RedisSessionFilter() {
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
        sessionStore = new SessionStore(jedisPool);
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request, response, sessionStore);
        filterChain.doFilter(requestWrapper, response);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
