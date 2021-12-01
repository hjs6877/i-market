package com.imarket.marketdomain.repository.member;

import com.imarket.marketdomain.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQueryRepository {
    Optional<Member> findByEmail(String email);
}
