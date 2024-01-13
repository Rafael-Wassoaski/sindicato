package com.rafaelwassoaski.sindicato.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;


@Service
public class HttpServletService {

    public HttpServletResponse setTokenCookie(HttpServletResponse response, String token) {

        ResponseCookie resCookie = createCookie(token, 3600);
        response.addHeader("Set-Cookie", resCookie.toString());

        return response;
    }

    private ResponseCookie createCookie(String token, int maxAge){
        return ResponseCookie.from("token", token)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(Math.toIntExact(maxAge))
                .build();
    }

    public HttpServletResponse removeTokenCookie(HttpServletResponse response) {
        response.addHeader("Set-Cookie", null);
        return response;
    }
}
