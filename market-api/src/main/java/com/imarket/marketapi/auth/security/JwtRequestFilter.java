package com.imarket.marketapi.auth.security;

import com.imarket.marketapi.auth.config.RedisInfo;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    private JwtTokenUtil jwtTokenUtil;
    private JwtTokenUtil jtu;
    private RedisTemplate<String, Object> redisTemplate;
    private RedisInfo redisInfo;

    @Autowired
    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil,
                            JwtTokenUtil jtu,
                            RedisTemplate<String, Object> redisTemplate,
                            RedisInfo redisInfo) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jtu = jtu;
        this.redisTemplate = redisTemplate;
        this.redisInfo = redisInfo;
    }

    public Authentication getAuthentication(String token) {
        Map<String, Object> parseInfo = jtu.getUserParseInfo(token);
        List<String> rs =(List)parseInfo.get("role");
        Collection<GrantedAuthority> tmp= new ArrayList<>();
        for (String a: rs) {
            tmp.add(new SimpleGrantedAuthority(a));
        }
        UserDetails userDetails = User.builder().username(String.valueOf(parseInfo.get("username"))).authorities(tmp).password("").build();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        return usernamePasswordAuthenticationToken;
    }

    @Bean
    public FilterRegistrationBean JwtRequestFilterRegistration (JwtRequestFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");
        log.info("tokenHeader: " + requestTokenHeader);
        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);

            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                // TODO Exception 처리
                log.warn("Unable to get JWT Token");
            }
            catch (ExpiredJwtException e) {
            }
        } else {
            // TODO Exception 처리
            log.warn("JWT Token does not begin with Bearer String");
        }

        log.info("redisInfo.radisEnable:" + redisInfo.redisEnable);

        if (username == null) {
            // TODO Exception 처리
            log.info("token maybe expired: username is null.");
        } else if (redisInfo.redisEnable && redisTemplate.opsForValue().get(jwtToken) != null) {
            // TODO Exception 처리
            log.warn("this token already logout!");
        } else {
            //DB access 대신에 파싱한 정보로 유저 만들기!
            Authentication authen =  getAuthentication(jwtToken);

            //만든 authentication 객체로 매번 인증받기
            SecurityContextHolder.getContext().setAuthentication(authen);
            //response.setHeader("username", username);
        }
        chain.doFilter(request, response);
    }
}
