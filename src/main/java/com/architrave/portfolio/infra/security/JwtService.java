package com.architrave.portfolio.infra.security;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.global.exception.custom.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    private SecretKey SECRET_KEY;

    @Value("${spring.jwt.expiration}")
    private Long jwtExpiration;

    @Value("${spring.jwt.refresh-token.expiration}")
    private Long refreshExpiration;

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
        try{
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // 토큰이 만료된 경우
        } catch (JwtException e) {
            // 다른 JWT 예외 처리
            throw new InvalidTokenException("Invalid JWT token", e);
        }

    }

    public long getLeftTime(String token){
        Date expirationDate = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload().getExpiration();

        Date now = new Date();

        return expirationDate.getTime() - now.getTime(); // 밀리초 단위
    }

    public String createJwt(Member member) {
        return createJwt(new HashMap<>(), member);
    }

    public String createJwt(Map<String, Object> extraClaims, Member member){
        return buildToken(extraClaims, member, jwtExpiration);
    }
    public String createRefreshToken(Member member){
        return buildToken(new HashMap<>(), member, refreshExpiration);
    }
    private String buildToken(Map<String, Object> extraClaims,
                              Member member,
                              Long expiration){
        return Jwts.builder()
                .claims(extraClaims)
                .subject(member.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SECRET_KEY)
                .compact();
    }
}
