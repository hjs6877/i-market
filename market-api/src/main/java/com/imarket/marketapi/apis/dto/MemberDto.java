package com.imarket.marketapi.apis.dto;

import com.imarket.marketdomain.domain.Member;
import com.imarket.marketdomain.domain.Order;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class MemberDto {
    private long memberId;
    private String email;
    private String name;
    private String nickName;
    private String phone;
    private Member.Gender gender;
    private long sellerId;
    private long buyerId;
    private OrderDto order;

    @Data
    public static class Post {
        private final String PASSWORD_MESSAGE =
                "비밀번호는 대,소문자와 숫자, 특수문자가 최소 1개 이상 포함된 8자 ~ 20자의 비밀번호여야 합니다.";
        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}",
                message = PASSWORD_MESSAGE)
        private String password;

        @NotBlank(message = "비밀 번호 확인은 필수 입력값입니다.")
        @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?=\\S+$).{8,20}",
                message = PASSWORD_MESSAGE)
        private String verifiedPassword;

        @NotBlank(message = "회원 이름은 필수 입력값입니다.")
        private String name;
        private String nickName;

        @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
        private String phone;

        @NotBlank(message = "성별은 필수 입력값입니다.")
        private String gender;
    }

    @Data
    public static class Patch {
        // TODO implementation
    }

    @Data
    public static class Search {
        private String email;
        private String name;
    }

    public MemberDto toMemberDto(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.name = member.getName();
        this.nickName = member.getNickName();
        this.phone = member.getPhone();
        this.gender = member.getGender();
        this.sellerId = member.getSeller().getSellerId();
        this.buyerId = member.getBuyer().getBuyerId();

        // Requirement: 각 회원의 마지막 주문 정보가 포함되어야 한다.
        List<Order> orderList = member.getBuyer().getOrders();
        if (orderList.size() > 0) {
            OrderDto orderDto = new OrderDto();
            this.order = orderDto.toOrderDto(orderList.get(orderList.size() - 1));
        }
        return this;
    }
}
