package com.imarket.marketapi.apis;

import com.imarket.marketapi.apis.dto.AuthDto;
import com.imarket.marketapi.apis.dto.LoginResult;
import com.imarket.marketapi.apis.dto.SToken;
import com.imarket.marketapi.apis.response.SingleResponse;
import com.imarket.marketapi.auth.config.RedisInfo;
import com.imarket.marketapi.auth.domain.Token;
import com.imarket.marketapi.auth.security.JwtInfo;
import com.imarket.marketapi.auth.security.JwtTokenUtil;
import com.imarket.marketapi.auth.service.Authenticator;
import com.imarket.marketapi.auth.service.JwtUserDetailsService;
import com.imarket.marketdomain.exception.BusinessLogicException;
import com.imarket.marketapi.auth.service.MarketUser;
import com.imarket.marketdomain.exception.ExceptionType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/api/${api.version}")
public class AuthController {
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final Authenticator authenticator;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtInfo jwtInfo;
    private final RedisInfo redisInfo;

    @Autowired
    public AuthController(JwtUserDetailsService userDetailsService,
                          JwtTokenUtil jwtTokenUtil,
                          Authenticator authenticator,
                          RedisTemplate<String, Object> redisTemplate,
                          JwtInfo jwtInfo,
                          RedisInfo redisInfo) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticator = authenticator;
        this.redisTemplate = redisTemplate;
        this.jwtInfo = jwtInfo;
        this.redisInfo = redisInfo;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<SingleResponse<LoginResult>> login(@RequestBody AuthDto authDto) throws Exception {
        final String username = authDto.getEmail();

        UserDetails userDetails = authenticator.authenticate(username, authDto.getPassword());

        List<String> tokens = authenticator.createToken(userDetails, username);
        final String accessToken = tokens.get(0);
        final String refreshToken = tokens.get(1);

        authenticator.setUpRedisForToken(username, refreshToken);

        Long memberId = ((MarketUser)userDetails).getMemberId();
        Long buyerId = ((MarketUser)userDetails).getBuyerId();
        Long sellerId = ((MarketUser)userDetails).getSellerId();

        LoginResult loginResult = new LoginResult(memberId, buyerId, sellerId, accessToken, refreshToken);

        return ResponseEntity.ok(new SingleResponse(HttpStatus.OK, loginResult));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<SingleResponse<Integer>> logout(@RequestHeader("Authorization") String accessToken) {
        String username = null;
        try {
            if (accessToken != null && accessToken.startsWith("Bearer ")) {
                accessToken = accessToken.substring(7);
                log.info("token in requestfilter: " + accessToken);
            } else {
                throw new BusinessLogicException("JWT Token does not begin with Bearer String", ExceptionType.UNAUTHORIZED);
            }
            username = jwtTokenUtil.getUsernameFromToken(accessToken);

        } catch (IllegalArgumentException e) {} catch (ExpiredJwtException e) { //expire됐을 때
            // TODO Exception 처리 수정
            username = e.getClaims().getSubject();
            log.info("username from expired access token: " + username);
        }

        if (redisInfo.redisEnable) {
            try {
                if (redisInfo.redisEnable && redisTemplate.opsForValue().get(username) != null) {
                    //delete refresh token
                    redisTemplate.delete(username);
                    redisTemplate.opsForValue().set(accessToken, new Token());
                    redisTemplate.expire(accessToken, jwtInfo.JWT_ACCESS_TOKEN_VALIDITY, TimeUnit.MINUTES);
                }
            } catch (IllegalArgumentException e) {
                log.warn("user does not exist");
            }
        }

        return ResponseEntity.ok(new SingleResponse(HttpStatus.OK, null));
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<SingleResponse<SToken>> refresh(@RequestBody SToken token) {
        String accessToken;
        String newToken;
        try {
            accessToken = token.getAccessToken();
            final String refreshToken = token.getRefreshToken();
            log.info("access token in rnat: " + accessToken);
            final String username = getUsernameFromAccessToken(accessToken);

            existRefreshToken(refreshToken);

            if (redisInfo.redisEnable) {
                String refreshTokenFromDb = getRefreshTokenFromDB(username);
                verifyRefreshToken(() -> !refreshToken.equals(refreshTokenFromDb) || jwtTokenUtil.isTokenExpired(refreshToken));
                newToken = renewToken(username);
                expireAccessToken(accessToken);
            } else {
                final String refreshUsername = jwtTokenUtil.getUsernameFromToken(refreshToken);
                verifyRefreshToken(() -> !refreshUsername.equals(username) || jwtTokenUtil.isTokenExpired(refreshToken));
                newToken = renewToken(username);
            }

        } catch (Exception e) {
            throw new BusinessLogicException("your refresh token does not exist.", ExceptionType.UNAUTHORIZED);
        }

        return ResponseEntity.ok(new SingleResponse(HttpStatus.OK, new SToken(newToken, null)));
    }

    private String getUsernameFromAccessToken(String accessToken) {
        String username = null;
        try {
            username = jwtTokenUtil.getUsernameFromToken(accessToken);
        } catch (IllegalArgumentException e) {

        } catch (ExpiredJwtException e) { //expire됐을 때
            username = e.getClaims().getSubject();
            log.info("username from expired access token: " + username);
        }
        return username;
    }
    private void expireAccessToken(String accessToken) {
        redisTemplate.opsForValue().set(accessToken, true);
        redisTemplate.expire(accessToken, jwtInfo.JWT_ACCESS_TOKEN_VALIDITY, TimeUnit.MINUTES);
    }

    private String renewToken(String username) {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newToken = jwtTokenUtil.generateAccessToken(userDetails);
        return newToken;
    }

    private void verifyRefreshToken(PredicateRefreshToken predicate) {
        if (predicate.test()) {
            throw new BusinessLogicException("refresh token is expired.", ExceptionType.UNAUTHORIZED);
        }
    }


    private String getRefreshTokenFromDB(String username) {
        String refreshTokenFromDb = null;
        try {
            ValueOperations<String, Object> vop = redisTemplate.opsForValue();
            Token result = (Token) vop.get(username);
            refreshTokenFromDb = result.getRefreshToken();
            log.info("rtfrom db: " + refreshTokenFromDb);
        } catch (IllegalArgumentException e) {
            log.warn("illegal argument!!");
        }
        return refreshTokenFromDb;
    }

    private void existRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new BusinessLogicException("your refresh token does not exist.", ExceptionType.UNAUTHORIZED);
        }
    }
}

interface PredicateRefreshToken {
    boolean test();
}
