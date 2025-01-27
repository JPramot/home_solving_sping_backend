package com.spring.home_solver.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpireMs}")
    private int jwtExpireMs;

    private final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    protected String  getJwtFromHeader(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");

        if(bearerToken.startsWith("Bearer ")) {
            return bearerToken.split(" ")[1];
        }
        logger.error("wrong format token: {}",bearerToken);
        return null;
    }

    public String generateJwtFromUsername(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Instant now = Instant.now();
        String claim = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        return Jwts.builder()
                .claim("roles",claim)
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtExpireMs)))
                .signWith(key())
                .compact();
    }


    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
    }

    protected boolean validateJwt(String jwt) {
        try{
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(jwt);

            return true;
        }catch (MalformedJwtException exc){
            logger.error("jwt malForm: {}",exc.getMessage());
        }catch (ExpiredJwtException exc) {
            logger.error("jwt expire: {}",exc.getMessage());
        }catch (UnsupportedJwtException exc) {
            logger.error("jwt unSupported: {}",exc.getMessage());
        }catch (IllegalArgumentException exc) {
            logger.error("illegalArgument: {}" ,exc.getMessage());
        }
        return false;
    }

    protected String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


}
