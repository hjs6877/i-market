package com.imarket.marketapi.apis.restdocs;

import com.imarket.marketapi.apis.ProductController;
import com.imarket.marketapi.apis.dto.ProductDto;
import com.imarket.marketapi.apis.helper.MockData;
import com.imarket.marketapi.apis.helper.ProductControllerTestHelper;
import com.imarket.marketapi.auth.security.JwtRequestFilter;
import com.imarket.marketdomain.domain.Product;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;

import static com.imarket.marketapi.apis.utils.ApiDocumentUtils.getDocumentRequest;
import static com.imarket.marketapi.apis.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = {ProductController.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class}))
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest implements ProductControllerTestHelper {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    private String accessToken;
    private MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

    @BeforeEach
    public void setup() {
        accessToken = getAccessToken();
        queryParams.add("page", String.valueOf(0));
        queryParams.add("size", String.valueOf(10));
    }

    @Test
    public void postProductTest() throws Exception {
        // given
        Product responseProduct = MockData.MockProduct.get();

        given(productService.saveProduct(Mockito.any())).willReturn(responseProduct);

        // when
        ProductDto.Post productPostDto = new ProductDto.Post();
        productPostDto.setSellerId(1);
        productPostDto.setProductCategoryId(1);
        productPostDto.setProductName("LG 냉장고");
        productPostDto.setDescription("LG 냉장고 좋아요");
        productPostDto.setPrice(2000000);


        String content = toJsonContent(productPostDto);

        ResultActions result =
                mvc.perform(postRequestBuilder("/api/v1/products",
                        content, MediaType.APPLICATION_JSON, accessToken));

        List<FieldDescriptor> productRequestDescriptors = getDefaultProductPostRequestDescriptors();

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productId").value(1))
                .andDo(document("post-product",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                getDefaultRequestHeaderDescriptor()
                        ),
                        requestFields(productRequestDescriptors),
                        responseFields(
                                getFullResponseDescriptors(getProductResponseDescriptors(DataResponseType.SINGLE))
                        )
                ));
    }

    @Test
    public void getProductTest() throws Exception {
        // given
        Product responseProduct = MockData.MockProduct.get();
        given(productService.findProductById(1)).willReturn(responseProduct);

        // when
        long productId = 1;

        mvc.perform(getRequestBuilder("/api/v1/products/{product-id}", productId, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.productId").value(1))
                .andDo(document("get-product",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("product-id").description("상품 ID")
                        ),
                        responseFields(
                                getFullResponseDescriptors(getProductResponseDescriptors(DataResponseType.SINGLE))
                        )
                ));
    }

    @Test
    public void getAllProductsTest() throws Exception {
        // given
        Product responseProduct = MockData.MockProduct.get();
        Page<Product> productPage =
                new PageImpl(Arrays.asList(responseProduct),
                        PageRequest.of(0, 10, Sort.by("cratedAt").descending()), 10);
        given(productService.searchProduct(null, null, 0, 10))
                .willReturn(productPage);

        mvc.perform(getRequestBuilder("/api/v1/products", queryParams, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-all-products",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("page").description("Page 번호"),
                                parameterWithName("size").description("Page Size"),
                                parameterWithName("productName").description("상품명").optional(),
                                parameterWithName("description").description("상품 설명").optional()
                        ),
                        responseFields(
                                getFullPageResponseDescriptors(getProductResponseDescriptors(DataResponseType.LIST))
                        )
                ));
    }
}
