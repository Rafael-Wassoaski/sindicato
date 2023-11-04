package com.rafaelwassoaski.sindicato.service.secutiry;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.rafaelwassoaski.sindicato.entity.CustomUser;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JWTService {

    @Value("${security.jwt.expiration}")
    private String expirationHours;

    @Value("${security.jwt.signatureKey}")
    private String tokenSiganture;

    public String generateToken (CustomUser customUser){
        long expirationTime = Long.parseLong(expirationHours);
        LocalDateTime timeOfExpiration = LocalDateTime.now().plusHours(expirationTime);
        Instant instanteOfExpiration = timeOfExpiration.atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(instanteOfExpiration);

        return Jwts
                .builder()
                .setSubject(customUser.getEmail())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, this.tokenSiganture)
                .compact();
    }

    private Claims getClaims(String token) throws ExpiredJwtException{
        return Jwts
                .parser()
                .setSigningKey(this.tokenSiganture)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsername(String token){
        Claims claims = this.getClaims(token);

        return claims.getSubject();
    }

    public boolean isTokenValid(String token){
        try{
            Claims claims = this.getClaims(token);
            Date expirationDate = claims.getExpiration();
            LocalDateTime expirationTime = expirationDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            return !LocalDateTime.now().isAfter(expirationTime);
        }catch(Exception exception){
            System.out.println("Erro ao validar token: " + exception.getMessage());
            return false;
        }
    }
}