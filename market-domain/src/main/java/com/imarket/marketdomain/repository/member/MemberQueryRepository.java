package com.imarket.marketdomain.repository.member;

import com.imarket.marketdomain.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryRepository {
    Page<Member> searchMember(String email, String name, Pageable pageable);
}
