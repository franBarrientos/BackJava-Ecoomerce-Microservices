package com.ecommerce.Domain.Infrastructure.Config.Spring;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.key}")
    private String jwtKey;
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Value("${jwt.refresh-token}")
    private long refreshExpiration;

    public String extractUsername(String jwtToken) {
        return extratClaim(jwtToken, Claims::getSubject);
    }
    public Date extractExpiration(String jwtToken) {
        return extratClaim(jwtToken, Claims::getExpiration);
    }

    public <T> T extratClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }


    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwtToken).getBody();
    }


    private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

    public String generateToken(
            Map<String, Object> extraInformationClaims,
            UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraInformationClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + getJwtExpiration()))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(
            UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }




    public boolean isTokenValid(String jwtToken, UserDetails userDetails){
        final String userEmail = extractUsername(jwtToken);
        return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken);
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    public String getJwtKey() {
        return this.jwtKey;
    }

    public void setJwtKey(String jwtKey) {
        this.jwtKey = jwtKey;
    }

    public long getJwtExpiration() {
        return this.jwtExpiration;
    }

    public void setJwtExpiration(long jwtExpiration) {
        this.jwtExpiration = jwtExpiration;
    }

    public long getRefreshExpiration() {
        return this.refreshExpiration;
    }

    public void setRefreshExpiration(long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }

}
