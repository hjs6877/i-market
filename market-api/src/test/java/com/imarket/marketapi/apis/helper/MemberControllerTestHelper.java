package com.imarket.marketapi.apis.helper;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public interface MemberControllerTestHelper extends ControllerTestHelper {
    default List<ParameterDescriptor> getMemberRequestPathParameterDescriptor() {
        return Arrays.asList(parameterWithName("member-id").description("회원 식별자 ID"));
    }

    default List<FieldDescriptor> getDefaultMemberPostRequestDescriptors() {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        descriptors.add(fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"));
        descriptors.add(fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드"));
        descriptors.add(fieldWithPath("verifiedPassword").type(JsonFieldType.STRING).description("패스워드 확인"));
        descriptors.add(fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"));
        descriptors.add(fieldWithPath("nickName").type(JsonFieldType.STRING).description("회원 별칭"));
        descriptors.add(fieldWithPath("phone").type(JsonFieldType.STRING).optional().description("전화번호"));
        descriptors.add(fieldWithPath("gender").type(JsonFieldType.STRING).optional().description("성별"));

        return descriptors;
    }

    default List<FieldDescriptor> getMemberResponseDescriptors(DataResponseType dataResponseType) {
        String parentPath = getDataParentPath(dataResponseType);
        return Arrays.asList(
                fieldWithPath(parentPath.concat("memberId")).type(JsonFieldType.NUMBER).description("회원 식별 ID"),
                fieldWithPath(parentPath.concat("email")).type(JsonFieldType.STRING).description("이메일(로그인 ID)"),
                fieldWithPath(parentPath.concat("name")).type(JsonFieldType.STRING).description("회원 이름"),
                fieldWithPath(parentPath.concat("nickName")).type(JsonFieldType.STRING).description("회원 별칭"),
                fieldWithPath(parentPath.concat("phone")).type(JsonFieldType.STRING).description("회원 전화번호"),
                fieldWithPath(parentPath.concat("gender")).type(JsonFieldType.STRING).optional().description("성별"),
                fieldWithPath(parentPath.concat("sellerId"))
                        .type(JsonFieldType.NUMBER).description("판매자 ID").optional(),
                fieldWithPath(parentPath.concat("buyerId"))
                        .type(JsonFieldType.NUMBER).description("구매자 ID").optional(),
                fieldWithPath(parentPath.concat("order"))
                        .type(JsonFieldType.OBJECT).description("마지막 주문 정보").optional()
        );
    }
}
