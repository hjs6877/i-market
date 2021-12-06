package com.imarket.marketapi.apis.helper;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public interface OrderControllerTestHelper extends ControllerTestHelper {
    default List<ParameterDescriptor> getOrderRequestPathParameterDescriptor() {
        return Arrays.asList(parameterWithName("order-id").description("주문 ID"));
    }

    default List<FieldDescriptor> getDefaultOrderPostRequestDescriptors() {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        descriptors.add(fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 ID"));
        descriptors.add(fieldWithPath("buyerId").type(JsonFieldType.NUMBER).description("구매자 ID"));
        descriptors.add(fieldWithPath("sellerId").type(JsonFieldType.NUMBER).description("판매자 ID"));
        descriptors.add(fieldWithPath("paymentId").type(JsonFieldType.NUMBER).description("지불 카드 ID"));
        descriptors.add(fieldWithPath("amount").type(JsonFieldType.NUMBER).description("주문 수량"));

        return descriptors;
    }

    default List<FieldDescriptor> getOrderResponseDescriptors(DataResponseType dataResponseType) {
        String parentPath = getDataParentPath(dataResponseType);
        return Arrays.asList(
                fieldWithPath(parentPath.concat("orderId")).type(JsonFieldType.NUMBER).description("주문 ID"),
                fieldWithPath(parentPath.concat("email")).type(JsonFieldType.STRING).description("이메일").optional(),
                fieldWithPath(parentPath.concat("name")).type(JsonFieldType.STRING).description("회원 이름").optional(),
                fieldWithPath(parentPath.concat("orderNumber")).type(JsonFieldType.STRING).description("주문 번호"),
                fieldWithPath(parentPath.concat("orderStatus")).type(JsonFieldType.STRING).description("주문 상태"),
                fieldWithPath(parentPath.concat("productName"))
                        .type(JsonFieldType.STRING).description("상품명").optional(),
                fieldWithPath(parentPath.concat("amount")).type(JsonFieldType.NUMBER).description("주문 수량"),
                fieldWithPath(parentPath.concat("createdAt")).type(JsonFieldType.STRING).description("주문 날짜")

        );
    }
}
