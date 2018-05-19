package com.acupt.acupsession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by liujie on 2018/5/10.
 */
public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private SessionWrapper session;
    private SessionStore sessionStore;

    public HttpServletRequestWrapper(HttpServletRequest request, HttpServletResponse response, SessionStore sessionStore) {
        super(request);
        this.request = request;
        this.response = response;
        this.sessionStore = sessionStore;
        initSession();
    }

    private void initSession() {
        String id = getSessionIdFromCookie();
        if (id != null && !id.equals("")) {
            SessionContext context = sessionStore.find(id);
            if (context != null) {
                context.setLastAccessedTime(System.currentTimeMillis());
                session = new SessionWrapper(context, sessionStore, request.getServletContext());
                return;
            }
        }
        session = createSession();
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (session == null && create) {
            session = createSession();
        }
        if (session != null) {
            session.touch();
        }
        return session;
    }

    private SessionWrapper createSession() {
        SessionContext context = sessionStore.create();
        response.addCookie(new Cookie(SessionConfig.getCookieSessionid(), context.getId()));
        return new SessionWrapper(context, sessionStore, request.getServletContext());
    }

    private String getSessionIdFromCookie() {
        for (Cookie cookie : request.getCookies()) {
            if (SessionConfig.getCookieSessionid().equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
