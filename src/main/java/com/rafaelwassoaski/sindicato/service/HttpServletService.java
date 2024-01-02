package com.rafaelwassoaski.sindicato.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;


@Service
public class HttpServletService {

    public HttpServletResponse setTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie resCookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .maxAge(Math.toIntExact(3600))
                .build();

        response.addHeader("Set-Cookie", resCookie.toString());

        return response;

    }
}
