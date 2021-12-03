package com.imarket.marketapi.apis.helper;

import com.imarket.marketdomain.domain.*;

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

}
