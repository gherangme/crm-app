package filter;

import payload.BasicResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

public class CookieFunction {

    final String COOKIE_NAME = "username";

    // Tạo cookie
    public void createCookie(HttpServletResponse resp, String email) {
        Cookie cookie = new Cookie(COOKIE_NAME, email);
        cookie.setMaxAge(4 * 60 * 60);
        resp.addCookie(cookie);
    }

    // Xoá cookie
    public void deleteCookie(HttpServletResponse resp, Cookie cookie) {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    // Lấy thông tin cookie
    public Cookie getCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie ck : cookies) {
                if (COOKIE_NAME.equals(ck.getName())) {
                    return ck;
                }
            }
        }

        return null;
    }

    // Lấy thông tin email của user
    public String getUsernameByCookies(Cookie[] cookies) {
        if (cookies != null && cookies.length > 0) {
            for (Cookie ck : cookies) {
                if ("username".equals(ck.getName())) {
                    return ck.getValue();
                }
            }
        }

        return null;
    }

}
