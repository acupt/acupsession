package com.acupt.acupsession;

import java.io.Serializable;

/**
 * Created by liujie on 2018/5/18.
 */
public class SessionContext implements Serializable {

    private static final long serialVersionUID = -4949803399000657836L;

    private String id;
    private long creationTime = System.currentTimeMillis();
    private long lastAccessedTime = System.currentTimeMillis();
    private int maxInactiveInterval = SessionConfig.getSessionMaxInactiveInterval();

    public SessionContext(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

}
