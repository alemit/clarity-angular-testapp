package com.github.clarityangulartestapp.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.clarityangulartestapp.error.ErrorInfo;
import com.github.clarityangulartestapp.error.UnathorizedAccessException;
import com.github.clarityangulartestapp.model.User;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String REQUEST_BEAN = "requestUser";
    private static final String PUBLIC_URL = "/public/**";

    private List<String> excludeUrlPatterns;
    private PathMatcher pathMatcher;
    private ApplicationContext applicationContext;
    private JwtTokenManager jwtTokenManager;
    private CookieManager cookieManager;
    private ObjectMapper objMapper;

    @Autowired
    public JwtFilter(ApplicationContext applicationContext, JwtTokenManager jwtTokenManager, CookieManager cookieManager) {
        this.applicationContext = applicationContext;
        this.jwtTokenManager = jwtTokenManager;
        this.cookieManager = cookieManager;
        this.pathMatcher = new AntPathMatcher();
        this.objMapper = new ObjectMapper();

        this.excludeUrlPatterns = new ArrayList<>();
        this.excludeUrlPatterns.add(PUBLIC_URL);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = cookieManager.getCookieValue(request);
        if (token == null || "".equals(token)) {
            unauthorizedAccessResponse(response, "Missing authorization cookie");
            return;
        }

        String name = validateJwtToken(response, token);
        if (name == null) {
            unauthorizedAccessResponse(response, "Invalid authorization cookie");
            return;
        }
        renewJwtToken(response, name);
        chain.doFilter(request, response);
    }

    private void renewJwtToken(HttpServletResponse response, String name) {
        String cookieValue = null;
        try {
            cookieValue = jwtTokenManager.generateToken(name);
        } catch (UnsupportedEncodingException e) {
            logger.error("[renewJwtToken] error while creating JWT token: ", e);
        }
        cookieManager.addCookieToResponse(response, cookieValue);
    }

    private String validateJwtToken(HttpServletResponse response, String token) throws ServletException, IOException {
        try {
            Map<String, Claim> claims = jwtTokenManager.validateToken(token);
            User user = (User) applicationContext.getBean(REQUEST_BEAN);
            user.setUsername(claims.get("username").asString());
            return user.getUsername();
        } catch (final Exception e) {
            logger.error("[validateJwtToken] {}", e.getMessage());
        }
        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return excludeUrlPatterns.stream().anyMatch(p -> pathMatcher.match(p, request.getServletPath()));
    }

    private void unauthorizedAccessResponse(HttpServletResponse response, String message) throws IOException {
        UnathorizedAccessException exception = new UnathorizedAccessException(message);
        ErrorInfo errorInfo = new ErrorInfo(exception);
        String jsonObject = objMapper.writeValueAsString(errorInfo);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter out = response.getWriter();
        out.print(jsonObject);
        out.flush();
        out.close();
    }
}
