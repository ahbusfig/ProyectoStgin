package juego.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SessionCookieConfigListener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String contextPath = sce.getServletContext().getContextPath();
        sce.getServletContext().getSessionCookieConfig().setName("SESSIONID" + Math.random() * 10);
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        Cookie cookie = new Cookie("SESSIONID", String.valueOf(Math.random() * 10));
        HttpServletResponse response = (HttpServletResponse) se.getSession().getAttribute("response");
        response.addCookie(cookie);
    }
}

