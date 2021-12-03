package com.imarket.marketapi.apis.restdocs;

import com.imarket.marketapi.apis.dto.AuthDto;
import com.imarket.marketapi.apis.MemberController;
import com.imarket.marketapi.auth.security.JwtRequestFilter;
import com.imarket.marketapi.apis.dto.*;
import com.imarket.marketapi.apis.helper.MemberControllerTestHelper;
import com.imarket.marketapi.apis.helper.MockData;
import com.imarket.marketdomain.domain.Member;
import com.imarket.marketdomain.repository.member.MemberRepository;
import com.imarket.marketdomain.service.MemberService;

import com.imarket.marketdomain.service.OrderService;
import com.imarket.marketdomain.service.ProductService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static com.imarket.marketapi.apis.utils.ApiDocumentUtils.getDocumentRequest;
import static com.imarket.marketapi.apis.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = {MemberController.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class}))
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
//@ActiveProfiles("test")
public class MemberControllerTest implements MemberControllerTestHelper {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    public void setup() throws Exception {

    }

    @Test
    public void postUserTest() throws Exception {
        // given
        Member responseMember = MockData.MockMember.get();

        given(memberService.saveMember(Mockito.any())).willReturn(responseMember);

        // when
        MemberDto.Post memberPostDto = new MemberDto.Post();
        memberPostDto.setEmail("user@aaa.com");
        memberPostDto.setPassword("Hjs1234!");
        memberPostDto.setVerifiedPassword("Hjs1234!");
        memberPostDto.setName("user");
        memberPostDto.setNickName("Daddy");
        memberPostDto.setPhone("010-1111-1111");
        memberPostDto.setGender("MALE");

        String content = toJsonContent(memberPostDto);

        ResultActions result =
                mvc.perform(postRequestBuilder("/api/v1/members", content, MediaType.APPLICATION_JSON));

        List<FieldDescriptor> memberRequestDescriptors = getDefaultMemberPostRequestDescriptors();

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("user@aaa.com"))
                .andDo(document("post-member",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(memberRequestDescriptors),
                        responseFields(
                                getFullResponseDescriptors(getMemberResponseDescriptors(DataResponseType.SINGLE))
                        )
                ));


    }
}
