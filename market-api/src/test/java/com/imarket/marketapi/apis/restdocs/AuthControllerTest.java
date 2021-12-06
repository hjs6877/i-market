package com.imarket.marketapi.apis.restdocs;

import com.imarket.marketapi.apis.AuthController;
import com.imarket.marketapi.apis.dto.AuthDto;
import com.imarket.marketapi.apis.dto.SToken;
import com.imarket.marketapi.apis.helper.AuthControllerTestHelper;
import com.imarket.marketapi.apis.helper.MockData;
import com.imarket.marketapi.auth.config.RedisInfo;
import com.imarket.marketapi.auth.security.JwtInfo;
import com.imarket.marketapi.auth.security.JwtRequestFilter;
import com.imarket.marketapi.auth.security.JwtTokenUtil;
import com.imarket.marketapi.auth.service.Authenticator;
import com.imarket.marketapi.auth.service.JwtUserDetailsService;
import com.imarket.marketapi.auth.service.MarketUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static com.imarket.marketapi.apis.utils.ApiDocumentUtils.getDocumentRequest;
import static com.imarket.marketapi.apis.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = {AuthController.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class}))
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@MockBean({JwtUserDetailsService.class, RedisTemplate.class, JwtInfo.class, RedisInfo.class})
public class AuthControllerTest implements AuthControllerTestHelper {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Authenticator authenticator;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtUserDetailsService userDetailsService;

    @Test
    public void loginTest() throws Exception {
        // given
        MarketUser marketUser = MockData.MockMarketUser.get();
        List<String> tokens = Arrays.asList("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");

        given(authenticator.authenticate(Mockito.anyString(), Mockito.anyString()))
                .willReturn(marketUser);

        given(authenticator.createToken(Mockito.any(), Mockito.anyString()))
                .willReturn(tokens);

        doNothing().when(authenticator).setUpRedisForToken(Mockito.anyString(), Mockito.anyString());

        // when
        AuthDto authDto = new AuthDto();
        authDto.setEmail("user@aaa.com");
        authDto.setPassword("Aa12345678!");

        String content = toJsonContent(authDto);

        // then
        mvc.perform(postRequestBuilder("/api/v1/login", content, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("login",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(getDefaultUserLoginRequestDescriptors()),
                        responseFields(
                                getFullResponseDescriptors(getUserLoginResponseDescriptors())
                        )
                ));
    }

    @Test
    public void refreshTest() throws Exception {
        // given
        SToken sToken = MockData.MockSToken.get();
        String email = "user@aaa.com";

        given(jwtTokenUtil.getUsernameFromToken(Mockito.anyString()))
                .willReturn(email);

        given(jwtTokenUtil.isTokenExpired(Mockito.anyString()))
                .willReturn(false);

        given(userDetailsService.loadUserByUsername(Mockito.anyString()))
                .willReturn(null);

        given(jwtTokenUtil.generateAccessToken(Mockito.any()))
                .willReturn("cccccccccccccccccccccccccccccc");

        // when
        String content = toJsonContent(sToken);

        // then
        mvc.perform(postRequestBuilder("/api/v1/refresh", content, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("auth-token-refresh",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(getDefaultAuthTokenRequestDescriptors()),
                        responseFields(
                                getFullResponseDescriptors(getAuthTokenResponseDescriptors())
                        )
                ));
    }

    @Test
    public void logoutTest() throws Exception {
        // given
        String email = "user@aaa.com";
        given(jwtTokenUtil.getUsernameFromToken(Mockito.anyString()))
                .willReturn(email);

        // when
        String accessToken = getAccessToken();

        // then
        mvc.perform(postRequestBuilder("/api/v1/logout", MediaType.APPLICATION_JSON, accessToken))
                .andExpect(status().isOk())
                .andDo(document("logout",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                getDefaultRequestHeaderDescriptor()
                        ),
                        responseFields(
                                getDefaultResponseDescriptors(null)
                        )
                ));
    }
}
