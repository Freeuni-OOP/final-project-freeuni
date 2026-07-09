package com.oophotel.filter;

import com.oophotel.util.JsonUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

/**
 * Guards every /api/* route.
 * The three public paths below are allowed through without a session.
 * Everything else requires an active login session.
 */
@WebFilter("/api/*")
public class AuthFilter implements Filter {

    private static final Set<String> PUBLIC = Set.of(
            "/api/ping",
            "/api/db-check",
            "/api/auth/login",
            "/api/auth/register",
            "/api/contact"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req = (HttpServletRequest)  request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getServletPath();
        if (req.getPathInfo() != null) path += req.getPathInfo();

        if (PUBLIC.contains(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            JsonUtil.write(res, HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized — please sign in first");
            return;
        }

        chain.doFilter(request, response);
    }
}
