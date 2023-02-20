package com.swp.onlineLearning.Config;

import com.swp.onlineLearning.Model.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    private final static String secret = "javaInUse";
    private final static Long expiration = (long) 18000000.0;

    public static String generateToken(Account account) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setId(account.getGmail())
                .setSubject(account.getRoleUser().getName())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static String getRoleFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }
    public static String getIdFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getId();
    }

    public static boolean validateToken(String token, UserDetails userDetails) {
        String username = getIdFromToken(token);
        return (username.equals(userDetails.getUsername())) ; // && !isTokenExpired(token)
    }

    private static boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
        return expirationDate.before(new Date());
    }
}
