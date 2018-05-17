package com.acupt.acupsession;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by liujie on 2018/5/10.
 */
public class HttpServletRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {

    private static final String COOKIE_SESSION_ID = "acupsession_id";
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
        if (id == null || id.equals("")) {
            id = createSession();
        }
        session = new SessionWrapper(id, sessionStore, request.getServletContext());
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (session == null && create) {
            String id = createSession();
            session = new SessionWrapper(id, sessionStore, request.getServletContext());
        }
        if (session != null) {
            session.touch();
        }
        return session;
    }

    private String createSession() {
        String id = sessionStore.create();
        response.addCookie(new Cookie(COOKIE_SESSION_ID, id));
        return id;
    }

    private String getSessionIdFromCookie() {
        for (Cookie cookie : request.getCookies()) {
            if (COOKIE_SESSION_ID.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

}
