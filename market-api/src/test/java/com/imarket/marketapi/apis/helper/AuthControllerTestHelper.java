package com.imarket.marketapi.apis.helper;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public interface AuthControllerTestHelper extends ControllerTestHelper {
    default List<FieldDescriptor> getDefaultUserLoginRequestDescriptors() {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        descriptors.add(fieldWithPath("email").type(JsonFieldType.STRING).description("이메일)"));
        descriptors.add(fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드"));

        return descriptors;
    }

    default List<FieldDescriptor> getUserLoginResponseDescriptors() {
        return Arrays.asList(
                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("Member ID(식별자)"),
                fieldWithPath("data.sellerId").type(JsonFieldType.NUMBER).description("판매자 ID"),
                fieldWithPath("data.buyerId").type(JsonFieldType.NUMBER).description("구매자 ID"),
                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("Access Token"),
                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).optional().description("Refresh Token")
        );
    }

    default List<FieldDescriptor> getDefaultAuthTokenRequestDescriptors() {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        descriptors.add(fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Access Token"));
        descriptors.add(fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("Refresh Token"));

        return descriptors;
    }

    default List<FieldDescriptor> getAuthTokenResponseDescriptors() {
        return Arrays.asList(
                fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("Access Token"),
                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING).optional().description("Refresh Token")
        );
    }
}
