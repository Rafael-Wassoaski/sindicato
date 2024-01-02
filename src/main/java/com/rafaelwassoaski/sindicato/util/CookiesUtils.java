package com.rafaelwassoaski.sindicato.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class CookiesUtils {
    public static Optional<String> extractTokenCookie(HttpServletRequest request) {
        String token = null;
        if (request.getCookies() != null) {
            Cookie[] cookies = request.getCookies();

            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("token")) {
                    token = cookies[i].getValue();
                }
            }
        }

        if (token == null) {
            return Optional.empty();
        }

        return Optional.of(token);
    }
}
