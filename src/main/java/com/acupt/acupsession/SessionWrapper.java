package com.acupt.acupsession;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by liujie on 2018/5/17.
 */
public class SessionWrapper implements HttpSession {

    private String id;
    private SessionContext sessionContext;
    private transient SessionStore sessionStore;
    private transient ServletContext servletContext;

    public SessionWrapper(SessionContext sessionContext, SessionStore sessionStore, ServletContext servletContext) {
        this.id = sessionContext.getId();
        this.sessionContext = sessionContext;
        this.sessionStore = sessionStore;
        this.servletContext = servletContext;
    }

    public void touch() {
        sessionStore.touch(id);
    }

    public long getCreationTime() {
        return sessionContext.getCreationTime();
    }

    public String getId() {
        return id;
    }

    public long getLastAccessedTime() {
        return sessionContext.getLastAccessedTime();
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public void setMaxInactiveInterval(int interval) {
        sessionContext.setMaxInactiveInterval(interval);
    }

    public int getMaxInactiveInterval() {
        return sessionContext.getMaxInactiveInterval();
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
        return sessionStore.getAttributeNames(id);
    }

    public String[] getValueNames() {
        Enumeration<String> enumeration = getAttributeNames();
        List<String> list = new ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        String[] array = new String[list.size()];
        return list.toArray(array);
    }

    public void setAttribute(String name, Object value) {
        sessionStore.setAttribute(id, name, value);
    }

    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }

    public void removeAttribute(String name) {
        sessionStore.removeAttribute(id, name);
    }

    public void removeValue(String name) {
        removeAttribute(name);
    }

    public void invalidate() {
        sessionStore.invalidate(id);
    }

    public boolean isNew() {
        return false;
    }
}
