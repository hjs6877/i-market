package com.imarket.marketapi.auth.config;

import com.imarket.marketapi.auth.security.JwtAuthenticationEntryPoint;
import com.imarket.marketapi.auth.security.JwtRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Profile(value = {"local", "dev", "prod"})
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
    @Value("${security.ignore.uri}")
    private String ignoreUri;

    @Value("${security.enabled}")
    private boolean securityEnable;
    private PasswordEncoder passwordEncoder;

    private JwtRequestFilter jwtRequestFilter;

    private UserDetailsService jwtUserDetailsService;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfiguration(PasswordEncoder passwordEncoder,
                                 JwtRequestFilter jwtRequestFilter,
                                 UserDetailsService jwtUserDetailsService,
                                 JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.passwordEncoder = passwordEncoder;
        this.jwtRequestFilter = jwtRequestFilter;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        log.info("Spring Security Enable: " + securityEnable);
        if (!securityEnable) {
            http.
                    httpBasic()
                    .disable()
                    .cors()
                    .and().
                    csrf().disable().
                    sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/**").permitAll();
        } else {
            http.
                    httpBasic()
                    .disable()
                    .cors()
                    .and().
                    csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/*/login", "/*/refresh").permitAll()
                    .antMatchers("/docs/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/*/logout").hasAnyRole("USER")
                    .and() // 회원 설정
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/*/members/*").hasAnyRole("USER")
                    .antMatchers(HttpMethod.GET, "/*/members").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.POST, "/*/members").permitAll()
                    .and()  // 상품 설정
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/*/products/*").permitAll()
                    .antMatchers(HttpMethod.GET, "/*/products").permitAll()
                    .antMatchers(HttpMethod.POST, "/*/products").hasRole("USER")
                    .and()  // 구매자 설정
                    .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/*/buyers/*/orders").hasRole("USER")
                    .and()  // 주문 설정
                    .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/*/orders").hasRole("USER")
                    .antMatchers(HttpMethod.GET, "/*/orders/*").hasRole("USER")
                    .and()
                    .authorizeRequests()
                    .anyRequest().denyAll()
                    //.anyRequest().authenticated()
                    .and()
                    .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .and()
                    .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) {
        log.info("# ignoreUri: " + ignoreUri);
        web.ignoring().antMatchers(ignoreUri);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
