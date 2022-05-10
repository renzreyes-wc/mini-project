package com.example.demo.api.utils;

import com.example.demo.api.models.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static String SECRET_KEY = "secret";
    private static long expiryDuration = 60 * 60;

    public String generateJwt(AppUser user) {
        long milliTime = System.currentTimeMillis();
        long expirationTime = milliTime + expiryDuration * 1000;

        Date issuedAt = new Date(milliTime);
        Date expiration = new Date(expirationTime);

        Claims claims = Jwts.claims()
                .setIssuer(user.getId().toString())
                .setIssuedAt(issuedAt)
                .setSubject(user.getEmail())
                .setExpiration(expiration);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String verifyJwt(String token) throws Exception {
        System.out.println(token + " token");
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            System.out.println(claims + " claims");
            return claims.getSubject();
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token");
        }
    }
}
