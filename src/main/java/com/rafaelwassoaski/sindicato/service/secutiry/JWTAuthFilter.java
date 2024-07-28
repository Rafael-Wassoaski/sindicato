package com.rafaelwassoaski.sindicato.service.secutiry;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rafaelwassoaski.sindicato.entity.CustomUser;
import com.rafaelwassoaski.sindicato.exceptions.ResourceNotFoundException;
import com.rafaelwassoaski.sindicato.service.UserService;
import com.rafaelwassoaski.sindicato.util.CookiesUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

@Service
public class JWTAuthFilter extends OncePerRequestFilter {

    private JWTService jwtService;
    private UserService userServiceImplementation;

    public JWTAuthFilter(JWTService jwtService, UserService userServiceImplementation) {
        this.jwtService = jwtService;
        this.userServiceImplementation = userServiceImplementation;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> optionalToken = CookiesUtils.extractTokenCookie(request);

        if (optionalToken.isPresent()) {
            String token = optionalToken.get();
            boolean isTokenValid = jwtService.isTokenValid(token);

            if (isTokenValid) {
                String username = jwtService.getUsername(token);

                CustomUser customUser = null;
                try {
                    customUser = userServiceImplementation.findUserByEmail(username);

                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }

                String []roles = customUser.getUserRoleAsArray();

                UserDetails userDetails = User.builder()
                        .username(customUser.getEmail())
                        .password(customUser.getPassword())
                        .roles(roles)
                        .build();
                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(customUser, null, userDetails.getAuthorities());

                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(user);
            }
        }

        filterChain.doFilter(request, response);
    }
}