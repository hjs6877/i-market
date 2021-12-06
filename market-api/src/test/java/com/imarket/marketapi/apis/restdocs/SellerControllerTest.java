package com.imarket.marketapi.apis.restdocs;

import com.imarket.marketapi.apis.SellerController;
import com.imarket.marketapi.apis.helper.MockData;
import com.imarket.marketapi.apis.helper.OrderControllerTestHelper;
import com.imarket.marketapi.auth.security.JwtRequestFilter;
import com.imarket.marketdomain.domain.Order;
import com.imarket.marketdomain.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

import static com.imarket.marketapi.apis.utils.ApiDocumentUtils.getDocumentRequest;
import static com.imarket.marketapi.apis.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest(controllers = {SellerController.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class}))
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class SellerControllerTest implements OrderControllerTestHelper {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    private String accessToken;
    private MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

    @BeforeEach
    public void setup() {
        accessToken = getAccessToken();
        queryParams.add("page", String.valueOf(0));
        queryParams.add("size", String.valueOf(10));
    }

    @Test
    public void getOrdersBySellerTest() throws Exception {
        // given
        Order responseOrder = MockData.MockOrder.get();
        Page<Order> orderPage =
                new PageImpl(Arrays.asList(responseOrder),
                        PageRequest.of(0, 10, Sort.by("cratedAt").descending()), 10);
        given(orderService.findOrdersBySeller(1, 0, 10))
                .willReturn(orderPage);

        // when
        long sellerId = 1;

        mvc.perform(getRequestBuilder("/api/v1/sellers/{seller-id}/orders", sellerId,
                        queryParams, MediaType.APPLICATION_JSON, accessToken))
                .andExpect(status().isOk())
                .andDo(document("get-orders-by-seller",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                getDefaultRequestHeaderDescriptor()
                        ),
                        pathParameters(
                                parameterWithName("seller-id").description("판매자 ID")
                        ),
                        requestParameters(
                                parameterWithName("page").description("Page 번호"),
                                parameterWithName("size").description("Page Size")
                        ),
                        responseFields(
                                getFullPageResponseDescriptors(getOrderResponseDescriptors(DataResponseType.LIST))
                        )
                ));
    }
}
