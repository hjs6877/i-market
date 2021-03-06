package com.imarket.marketapi.auth.security;

import com.imarket.marketapi.auth.service.MarketUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;
    private final JwtInfo jwtInfo;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtInfo.secret).parseClaimsJws(token).getBody();
    }

    public Map<String, Object> getUserParseInfo(String token) {
        Claims parseInfo = Jwts.parser().setSigningKey(jwtInfo.secret).parseClaimsJws(token).getBody();
        Map<String, Object> result = new HashMap<>();
        result.put("username", parseInfo.getSubject());
        result.put("role", parseInfo.get("role", List.class));
        result.put("memberId", parseInfo.get("memberId"));
        result.put("sellerId", parseInfo.get("sellerId"));
        result.put("buyerId", parseInfo.get("buyerId"));
        return result;
    }


    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        System.out.println(expiration + "   " + new Date() + "   " + (expiration.before(new Date())));
        return expiration.before(new Date());
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> li = new ArrayList<>();

        for (GrantedAuthority a: userDetails.getAuthorities()) {
            li.add(a.getAuthority());
        }

        claims.put("role",li);
        claims.put("memberId", ((MarketUser)userDetails).getMemberId());
        claims.put("sellerId", ((MarketUser)userDetails).getSellerId());
        claims.put("buyerId", ((MarketUser)userDetails).getBuyerId());
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtInfo.JWT_ACCESS_TOKEN_VALIDITY * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtInfo.secret).compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtInfo.JWT_REFRESH_TOKEN_VALIDITY * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtInfo.secret).compact();
    }
}
