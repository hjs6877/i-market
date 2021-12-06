package com.imarket.marketapi.apis.restdocs;

import com.imarket.marketapi.apis.MemberController;
import com.imarket.marketapi.apis.OrderController;
import com.imarket.marketapi.apis.dto.MemberDto;
import com.imarket.marketapi.apis.dto.OrderDto;
import com.imarket.marketapi.apis.helper.MemberControllerTestHelper;
import com.imarket.marketapi.apis.helper.MockData;
import com.imarket.marketapi.apis.helper.OrderControllerTestHelper;
import com.imarket.marketapi.auth.security.JwtRequestFilter;
import com.imarket.marketdomain.domain.Member;
import com.imarket.marketdomain.domain.Order;
import com.imarket.marketdomain.service.MemberService;
import com.imarket.marketdomain.service.OrderService;
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
@WebMvcTest(controllers = {OrderController.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class}))
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest implements OrderControllerTestHelper {

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
    public void postOrderTest() throws Exception {
        // given
        Order responseOrder = MockData.MockOrder.get();

        given(orderService.saveOrder(Mockito.any())).willReturn(responseOrder);

        // when
        OrderDto.Post orderPostDto = new OrderDto.Post();
        orderPostDto.setProductId(1);
        orderPostDto.setBuyerId(1);
        orderPostDto.setSellerId(1);
        orderPostDto.setPaymentId(1);
        orderPostDto.setAmount(1);

        String content = toJsonContent(orderPostDto);

        ResultActions result =
                mvc.perform(postRequestBuilder("/api/v1/orders", content, MediaType.APPLICATION_JSON, accessToken));

        List<FieldDescriptor> orderRequestDescriptors = getDefaultOrderPostRequestDescriptors();

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId").value(1))
                .andDo(document("post-order",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                getDefaultRequestHeaderDescriptor()
                        ),
                        requestFields(orderRequestDescriptors),
                        responseFields(
                                getFullResponseDescriptors(getOrderResponseDescriptors(DataResponseType.SINGLE))
                        )
                ));
    }

    @Test
    public void getOrderTest() throws Exception {
        // given
        Order responseOrder = MockData.MockOrder.get();
        given(orderService.findOrderById(1)).willReturn(responseOrder);

        // when
        long orderId = 1;

        mvc.perform(getRequestBuilder("/api/v1/orders/{order-id}", orderId, MediaType.APPLICATION_JSON, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.orderId").value(1))
                .andDo(document("get-order",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                getDefaultRequestHeaderDescriptor()
                        ),
                        pathParameters(
                                parameterWithName("order-id").description("주문 ID")
                        ),
                        responseFields(
                                getFullResponseDescriptors(getOrderResponseDescriptors(DataResponseType.SINGLE))
                        )
                ));
    }
}
