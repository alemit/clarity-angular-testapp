package com.github.clarityangulartestapp.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    public static final String AUTH_COOCKIE = "auth_cookie";
    public static final String SET_COOKIE_HEADER = "Set-Cookie";

    public String createCookie(String value, boolean expire) {
        StringBuilder sb = new StringBuilder();

        sb.append(AUTH_COOCKIE).append("=\"").append(value).append('"');
        sb.append(";Path=/");
        if (expire) {
            sb.append(";Max-Age=0");
        }
        sb.append(";HttpOnly");
        return sb.toString();
    }

    public HttpHeaders createCookieHeaders(String value, boolean expire) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SET_COOKIE_HEADER, createCookie(value, expire));
        return headers;
    }

    public void addCookieToResponse(HttpServletResponse response, String value) {
        response.addHeader(SET_COOKIE_HEADER, createCookie(value, false));
    }

    public String getCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (AUTH_COOCKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
