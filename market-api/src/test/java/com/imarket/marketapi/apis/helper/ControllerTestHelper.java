package com.imarket.marketapi.apis.helper;

import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.restdocs.headers.HeaderDescriptor;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public interface ControllerTestHelper<T> {
    default String getAccessToken() {
        return "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc2h3YW5nQHBlbnRhc2VjdXJpdHkuY29tIiwicm9sZSI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sInNlbGxlcklkIjoxNSwiYnV5ZXJJZCI6MTIsImV4cCI6MTYwNjQ1NDU3NywiaWF0IjoxNjA2NDUwOTc3LCJtZW1iZXJJZCI6Mjd9.1TvYDexLUkOkOsBksbS6dnyJ4Ig1m9LMdTJ2FzCdOW0GEEdM4S6MpLZTpMGZCa-BN9jnbC9htZljsi5e7Mc-OQ";
    }

    default RequestBuilder getRequestBuilder(String uri, MediaType mediaType) {
        return RestDocumentationRequestBuilders
                .get(uri)
                .accept(mediaType);
    }

    default RequestBuilder getRequestBuilder(String uri, long resourceId, MediaType mediaType) {
        return RestDocumentationRequestBuilders
                .get(uri, resourceId)
                .accept(mediaType);
    }

    default RequestBuilder getRequestBuilder(String uri, MediaType mediaType, String token) {
        return RestDocumentationRequestBuilders
                .get(uri)
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));
    }

    default RequestBuilder getRequestBuilder(String uri, long resourceId, MediaType mediaType, String token) {
        return RestDocumentationRequestBuilders
                .get(uri, resourceId)
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));
    }

    default RequestBuilder getRequestBuilder(String uri, MultiValueMap<String, String> queryParams, MediaType mediaType, String token) {
        return RestDocumentationRequestBuilders
                .get(uri)
                .params(
                        queryParams
                )
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));
    }

    default RequestBuilder getRequestBuilder(String uri, long resourceId, MultiValueMap<String, String> queryParams, MediaType mediaType, String token) {
        return RestDocumentationRequestBuilders
                .get(uri, resourceId)
                .params(
                        queryParams
                )
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));
    }

    default RequestBuilder getRequestBuilder(String uri, MultiValueMap<String, String> queryParams, MediaType mediaType) {
        return RestDocumentationRequestBuilders
                .get(uri)
                .params(
                        queryParams
                )
                .accept(mediaType);
    }

    default RequestBuilder getRequestBuilder(String uri, long resourceId, MultiValueMap<String, String> queryParams,
                                            MediaType mediaType) {
        return RestDocumentationRequestBuilders
                .get(uri, resourceId)
                .params(
                        queryParams
                )
                .accept(mediaType);
    }

    default RequestBuilder deleteRequestBuilder(String uri, String content, MediaType mediaType, String token) {
        return RestDocumentationRequestBuilders
                .delete(uri)
                .contentType(mediaType)
                .content(content)
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));
    }

    default RequestBuilder deleteRequestBuilder(String uri, long resourceId, MultiValueMap<String, String> queryParams,
                                               MediaType mediaType, String token) {
        return RestDocumentationRequestBuilders
                .delete(uri, resourceId)
                .params(
                        queryParams
                )
                .contentType(mediaType)
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));
    }

    default RequestBuilder deleteRequestBuilder(String uri, long resourceId, MediaType mediaType, String token) {
        return RestDocumentationRequestBuilders
                .delete(uri, resourceId)
                .contentType(mediaType)
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));
    }

    default RequestBuilder postRequestBuilder(String uri,
                                             String content,
                                             MediaType mediaType) {
        return RestDocumentationRequestBuilders
                .post(uri)
                .contentType(mediaType)
                .content(content)
                .accept(mediaType);

    }

    default RequestBuilder postRequestBuilder(String uri,
                                             String content,
                                             MediaType mediaType,
                                             String token) {
        return RestDocumentationRequestBuilders
                .post(uri)
                .contentType(mediaType)
                .content(content)
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));

    }

    default RequestBuilder postRequestBuilder(String uri,
                                             long resourceId,
                                             String content,
                                             MediaType mediaType,
                                             String token) {
        return RestDocumentationRequestBuilders
                .post(uri, resourceId)
                .contentType(mediaType)
                .content(content)
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));

    }

    default RequestBuilder postRequestBuilder(String uri,
                                              MediaType mediaType,
                                              String token) {
        return RestDocumentationRequestBuilders
                .post(uri)
                .contentType(mediaType)
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));

    }

    default RequestBuilder patchRequestBuilder(String uri,
                                              long resourceId,
                                              String content,
                                              MediaType mediaType,
                                              String token) {
        return RestDocumentationRequestBuilders
                .patch(uri, resourceId)
                .contentType(mediaType)
                .content(content)
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));

    }

    default RequestBuilder patchRequestBuilder(String uri,
                                              long resourceId,
                                              MultiValueMap<String, String> queryParams,
                                              String content,
                                              MediaType mediaType,
                                              String token) {


        return RestDocumentationRequestBuilders
                .patch(uri, resourceId)
                .params(
                        queryParams
                )
                .contentType(mediaType)
                .content(content)
                .accept(mediaType)
                .header("authorization", "Bearer ".concat(token));

    }

    default List<HeaderDescriptor> getDefaultRequestHeaderDescriptor() {
        return Arrays.asList(headerWithName("authorization").description(
                "인증에 필요한 토큰 정보<br>Bearer Authentication(RFC 6750) " +
                        "Access Token (Ex.  <b>Bearer eyJhbG...</b>)<br> Bearer 문자열을 access token 앞에 붙여야 한다."));
    }

    default List<FieldDescriptor> getDefaultResponseDescriptors(JsonFieldType jsonFieldTypeForData) {
        return Arrays.asList(
                fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메시지").optional(),
                fieldWithPath("status").type(JsonFieldType.NUMBER).description("결과 상태"),
                fieldWithPath("globalErrors").type(JsonFieldType.ARRAY).description("전체 에러").optional(),
                fieldWithPath("fieldErrors").type(JsonFieldType.ARRAY).description("Field 에러").optional(),
                fieldWithPath("data").type(jsonFieldTypeForData).description("결과 데이터").optional()
        );
    }

    default List<FieldDescriptor> getPageResponseDescriptors() {
        return Arrays.asList(
                fieldWithPath("page.pageNumber").type(JsonFieldType.NUMBER).description("페이지 번호").optional(),
                fieldWithPath("page.pageSize").type(JsonFieldType.NUMBER).description("페이지 사이즈").optional(),
                fieldWithPath("page.totalElements").type(JsonFieldType.NUMBER).description("전체 건 수").optional(),
                fieldWithPath("page.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수").optional()
        );
    }

    default List<FieldDescriptor> getFullResponseDescriptors(List<FieldDescriptor> dataResponseFieldDescriptors) {
        Stream<FieldDescriptor> defaultResponseDescriptors = getDefaultResponseDescriptors(JsonFieldType.OBJECT).stream();
        Stream<FieldDescriptor> dataResponseDescriptors = dataResponseFieldDescriptors.stream();
        return Stream.concat(defaultResponseDescriptors, dataResponseDescriptors)
                .collect(Collectors.toList());
    }

    default List<FieldDescriptor> getFullPageResponseDescriptors(List<FieldDescriptor> dataResponseFieldDescriptors) {
        Stream<FieldDescriptor> defaultResponseDescriptors = getDefaultResponseDescriptors(JsonFieldType.ARRAY).stream();
        Stream<FieldDescriptor> dataResponseDescriptors = dataResponseFieldDescriptors.stream();
        Stream<FieldDescriptor> pageResponseDescriptors = getPageResponseDescriptors().stream();

        Stream<FieldDescriptor> mergedStream =
                Stream.of(defaultResponseDescriptors, dataResponseDescriptors, pageResponseDescriptors)
                        .flatMap(descriptorStream -> descriptorStream);
        return mergedStream.collect(Collectors.toList());
    }

    default String getDataParentPath(DataResponseType dataResponseType) {
        return dataResponseType == DataResponseType.SINGLE ? "data." : "data[].";
    }

    default String toJsonContent(T t) {
        Gson gson = new Gson();
        String content = gson.toJson(t);
        return content;
    }


    enum DataResponseType {
        SINGLE, LIST
    }
}


