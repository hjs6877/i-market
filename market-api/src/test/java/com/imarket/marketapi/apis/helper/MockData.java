package com.imarket.marketapi.apis.helper;

import com.imarket.marketapi.apis.dto.SToken;
import com.imarket.marketapi.auth.service.MarketUser;
import com.imarket.marketdomain.domain.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Arrays;

public class MockData {
    public static class MockMember {
        public static Member get() {
            Member responseMember = new Member();
            Seller seller = new Seller();
            seller.setSellerId(1L);
            Buyer buyer = new Buyer();
            buyer.setBuyerId(1L);

            responseMember.setMemberId(1L);
            responseMember.setEmail("user@aaa.com");
            responseMember.setName("user");
            responseMember.setNickName("user nick name");
            responseMember.setPhone("010-1111-1111");
            responseMember.setGender(Member.Gender.MALE);
            responseMember.setSeller(seller);
            responseMember.setBuyer(buyer);

            return responseMember;
        }
    }

    public static class MockProduct {
        public static Product get() {
            Product responseProduct = new Product();

            responseProduct.setProductId(1L);
            responseProduct.setProductName("LG 냉장고");
            responseProduct.setDescription("LG 냉장고 좋아요");
            responseProduct.setPrice(2000000);
            responseProduct.setCanPurchase(true);

            return responseProduct;
        }
    }

    public static class MockOrder {
        public static Order get() {
            Order responseOrder = new Order();
            Member member = new Member();
            member.setEmail("buyer@aaa.com");
            member.setName("구매자1");
            Buyer buyer = new Buyer();
            buyer.setBuyerId(1L);
            buyer.setMember(member);
            Seller seller = new Seller();
            seller.setSellerId(1L);
            Product product = new Product();
            product.setProductId(1L);
            Payment payment = new Payment();
            payment.setPaymentId(1L);
            responseOrder.setOrderId(1L);

            responseOrder.setOrderId(1L);
            responseOrder.setBuyer(buyer);
            responseOrder.setSeller(seller);
            responseOrder.setProduct(product);
            responseOrder.setPayment(payment);
            responseOrder.setAmount(1);
            responseOrder.setOrderNumber("539C3B19-A6FD-4A96-9198-E32D4A2E8289");
            responseOrder.setCreatedAt(LocalDateTime.now());
            return responseOrder;
        }
    }

    public static class MockMarketUser {
        public static MarketUser get() {
            MarketUser marketUser = new MarketUser("user@aaa.com",
                    "Aa12345678!", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

            marketUser.setMemberId(1L);
            marketUser.setBuyerId(1L);
            marketUser.setSellerId(1L);

            return marketUser;
        }
    }

    public static class MockSToken {
        public static SToken get() {
            SToken sToken = new SToken();

            sToken.setAccessToken("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            sToken.setRefreshToken("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            return sToken;
        }
    }
}
