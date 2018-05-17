package com.acupt.acupsession;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * Created by liujie on 2018/5/17.
 */
public class SessionWrapper implements HttpSession {

    private String id;
    private transient SessionStore sessionStore;
    private transient ServletContext servletContext;

//    private long creationTime = System.currentTimeMillis();
//    private long lastAccessedTime;
//    private int maxInactiveInterval;

    public SessionWrapper(String id, SessionStore sessionStore, ServletContext servletContext) {
        this.id = id;
        this.sessionStore = sessionStore;
        this.servletContext = servletContext;
    }

    public void touch() {

    }

    public long getCreationTime() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public long getLastAccessedTime() {
        return 0;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setMaxInactiveInterval(int interval) {

    }

    public int getMaxInactiveInterval() {
        return 0;
    }

    public HttpSessionContext getSessionContext() {
        return null;
    }

    public Object getAttribute(String name) {
        return sessionStore.getAttribute(id, name);
    }

    public Object getValue(String name) {
        return getAttribute(name);
    }

    public Enumeration<String> getAttributeNames() {
        return null;
    }

    public String[] getValueNames() {
        return new String[0];
    }

    public void setAttribute(String name, Object value) {
        sessionStore.setAttribute(id,name,value);
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {

    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void invalidate() {

    }

    public boolean isNew() {
        return false;
    }
}
