package com.architrave.portfolio.infra.security;

import com.architrave.portfolio.domain.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private SecretKey SECRET_KEY;

    public JwtService(@Value("${spring.jwt.secret}") String secret){
        this.SECRET_KEY = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String extractEmailFromToken(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload().getExpiration().before(new Date());
    }

    public String createJwt(Member member) {
        return createJwt(new HashMap<>(), member);
    }

    public String createJwt(Map<String, Object> extraClaims, Member member){
        return Jwts.builder()
                .claims(extraClaims)
                .subject(member.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 15)) //1초 * 60 * 5 = 15분
                .signWith(SECRET_KEY)
                .compact();
    }
}
