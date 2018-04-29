package com.github.clarityangulartestapp.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;

@SpringBootTest
public class CookieManagerTest {

    private static final String COOKIE_VALUE = "cookieValue";
    private CookieManager cookieManager = new CookieManager();

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpServletRequest request;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void givenValidCookieValue_whenCreateCookie_thenReturnCookie() {
        String cookie = cookieManager.createCookie(COOKIE_VALUE, false);
        assertTrue(cookie.contains(COOKIE_VALUE));
        assertFalse(cookie.contains("Max-Age"));
    }

    @Test
    public void givenEmptyCookieValue_whenCreateCookie_thenReturnCookieWithMaxAge() {
        String cookie = cookieManager.createCookie("", true);
        assertTrue(cookie.contains("Max-Age"));
    }

    @Test
    public void givenValidCookieValue_whenCreateCookieHeaders_thenReturnCookieHeaders() {
        HttpHeaders cookieHeaders = cookieManager.createCookieHeaders(COOKIE_VALUE, false);
        assertTrue(cookieHeaders.containsKey(CookieManager.SET_COOKIE_HEADER));
    }

    @Test
    public void givenValidCookieValue_whenAddCookieToResponse_thenAddCookieHeaders() {
        cookieManager.addCookieToResponse(response, COOKIE_VALUE);
        verify(response, times(1)).addHeader(CookieManager.SET_COOKIE_HEADER, "auth_cookie=\"cookieValue\";Path=/;HttpOnly");
    }

    @Test
    public void givenRequestWithoutCookies_whenGetCookieValue_thenReturnNull() {
        when(request.getCookies()).thenReturn(null);
        String cookieValue = cookieManager.getCookieValue(request);
        assertNull(cookieValue);
    }

    @Test
    public void givenRequestWithAuthCookies_whenGetCookieValue_thenReturnCookieValue() {
        Cookie cookie = new Cookie(CookieManager.AUTH_COOCKIE, COOKIE_VALUE);
        Cookie[] cookies = {cookie};
        when(request.getCookies()).thenReturn(cookies);
        String cookieValue = cookieManager.getCookieValue(request);
        assertEquals(COOKIE_VALUE, cookieValue);
    }

    @Test
    public void givenRequestWithInvalidCookies_whenGetCookieValue_thenReturnNull() {
        Cookie cookie = new Cookie("invalid", COOKIE_VALUE);
        Cookie[] cookies = {cookie};
        when(request.getCookies()).thenReturn(cookies);
        String cookieValue = cookieManager.getCookieValue(request);
        assertNull(cookieValue);
    }
}
