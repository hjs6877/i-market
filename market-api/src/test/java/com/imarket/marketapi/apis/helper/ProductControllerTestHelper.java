package com.imarket.marketapi.apis.helper;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public interface ProductControllerTestHelper extends ControllerTestHelper {
    default List<ParameterDescriptor> getProductRequestPathParameterDescriptor() {
        return Arrays.asList(parameterWithName("product-id").description("상품 ID"));
    }

    default List<FieldDescriptor> getDefaultProductPostRequestDescriptors() {
        List<FieldDescriptor> descriptors = new ArrayList<>();
        descriptors.add(fieldWithPath("sellerId").type(JsonFieldType.NUMBER).description("판매자 ID"));
        descriptors.add(fieldWithPath("productCategoryId").type(JsonFieldType.NUMBER).description("상품 카테고리 ID"));
        descriptors.add(fieldWithPath("productName").type(JsonFieldType.STRING).description("상품명"));
        descriptors.add(fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품 가격"));
        descriptors.add(fieldWithPath("description").type(JsonFieldType.STRING).description("상품 설명"));

        return descriptors;
    }

    default List<FieldDescriptor> getProductResponseDescriptors(DataResponseType dataResponseType) {
        String parentPath = getDataParentPath(dataResponseType);
        return Arrays.asList(
                fieldWithPath(parentPath.concat("productId")).type(JsonFieldType.NUMBER).description("상품 ID"),
                fieldWithPath(parentPath.concat("productName")).type(JsonFieldType.STRING).description("상품명"),
                fieldWithPath(parentPath.concat("description")).type(JsonFieldType.STRING).description("상품 설명"),
                fieldWithPath(parentPath.concat("price")).type(JsonFieldType.NUMBER).description("상품 가격"),
                fieldWithPath(parentPath.concat("canPurchase")).type(JsonFieldType.BOOLEAN).description("구매가능 여부")
        );
    }
}
