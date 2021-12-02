package com.imarket.marketapi.auth.service;

import com.imarket.marketapi.auth.config.RedisInfo;
import com.imarket.marketapi.auth.domain.Token;
import com.imarket.marketapi.auth.security.JwtInfo;
import com.imarket.marketapi.auth.security.JwtTokenUtil;
import com.imarket.marketdomain.exception.BusinessLogicException;
import com.imarket.marketdomain.exception.ExceptionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Authenticator {
    private final AuthenticationManager am;
    private final JwtUserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RedisInfo redisInfo;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtInfo jwtInfo;

    @Autowired
    public Authenticator(JwtUserDetailsService userDetailsService,
                         AuthenticationManager am,
                         JwtTokenUtil jwtTokenUtil,
                         RedisInfo redisInfo,
                         RedisTemplate redisTemplate,
                         JwtInfo jwtInfo) {
        this.userDetailsService = userDetailsService;
        this.am = am;
        this.jwtTokenUtil = jwtTokenUtil;
        this.redisInfo = redisInfo;
        this.redisTemplate = redisTemplate;
        this.jwtInfo = jwtInfo;
    }

    public UserDetails authenticate(String username, String password) {
        try {
            am.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e){
            throw new BusinessLogicException(ExceptionType.UNAUTHORIZED);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return userDetails;
    }

    public List<String> createToken(UserDetails userDetails, String username) {
        final String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtTokenUtil.generateRefreshToken(username);

        log.info("generated access token: " + accessToken);
        log.info("generated refresh token: " + refreshToken);

        return Arrays.asList(accessToken, refreshToken);
    }

    public void setUpRedisForToken(String username, String refreshToken) {
        Token retok = new Token();
        retok.setUsername(username);
        retok.setRefreshToken(refreshToken);

        log.info("jwtInfo.JWT_REFRESH_TOKEN_VALIDITY: " + jwtInfo.JWT_REFRESH_TOKEN_VALIDITY);
        log.info("jwtInfo.JWT_ACCESS_TOKEN_VALIDITY: " + jwtInfo.JWT_ACCESS_TOKEN_VALIDITY);
        log.info("redisInfo.radisEnable: " + redisInfo.redisEnable);

        //generate Token and save in redis
        if (redisInfo.redisEnable) {
            ValueOperations<String, Object> vop = redisTemplate.opsForValue();
            vop.set(username, retok);
            redisTemplate.expire(username, jwtInfo.JWT_REFRESH_TOKEN_VALIDITY, TimeUnit.MINUTES);
        }
    }
//
//    public void verifySignUpStatus(Member.SignUpStatus signUpStatus) {
//        if (signUpStatus != Member.SignUpStatus.SIGN_UP_GRANT) {
//            throw new MemberNotGrantedException();
//        }
//    }
//
//    public void verifyAdmin(UserDetails userDetails) {
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
//
//        if (!roles.contains("ROLE_ADMIN")) {
//            throw new BusinessLogicException(ResponseCode.CAN_NOT_ACCESS_NON_ADMIN);
//        }
//    }
}
