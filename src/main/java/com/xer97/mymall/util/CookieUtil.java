package com.xer97.mymall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xer97
 * @date 2019/7/3 21:31
 */
@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = ".chen.com";
    private final static String COOKIE_NAME = "mmall_login_token";

    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");
        // 不设置maxage，cookie不会持久化到硬盘，只是存在内存中，只在当前页面有效
        // -1代表永久，单位为秒
        // 一年
        cookie.setMaxAge(60 * 60 * 24 * 365);
        log.info("write cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("read cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    log.info("return cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void deleteLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    // 设置成0,删除cookie
                    cookie.setMaxAge(0);
                    log.info("delete cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }


}
