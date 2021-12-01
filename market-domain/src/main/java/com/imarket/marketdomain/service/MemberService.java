package com.imarket.marketdomain.service;

import com.imarket.marketdomain.domain.Buyer;
import com.imarket.marketdomain.domain.Member;
import com.imarket.marketdomain.domain.Seller;
import com.imarket.marketdomain.exception.MemberDuplicatedException;
import com.imarket.marketdomain.exception.MemberNotFoundException;
import com.imarket.marketdomain.exception.PasswordNotMatchException;
import com.imarket.marketdomain.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public MemberService(MemberRepository memberRepository,
                         PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Member saveMember(Member member) {
        // 패스워드가 맞는지
        if (!member.getPassword().equals(member.getVerifiedPassword())) {
            throw new PasswordNotMatchException();
        }
        // 존재하는 멤버인지
        Optional<Member> duplicatedMember = memberRepository.findByEmail(member.getEmail());
        if (duplicatedMember.isPresent()) {
            throw new MemberDuplicatedException();
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setRoles(Arrays.asList("USER"));
        member.setBuyer(new Buyer());
        member.setSeller(new Seller());

        return memberRepository.save(member);
    }

    // Requirement: 단일 회원의 상세 정보를 조회할 수 있어야 한다.
    public Member findMemberById(long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        return member.orElseThrow(MemberNotFoundException::new);
    }

    public Page<Member> searchMember(String email,
                                     String name,
                                     int page,
                                     int size) {
        /**
         * Requirement
         *  - 페이지네이션으로 조회할 수 있어야 한다.
         *  - 이름, 이메일을 이용하여 검색을 할 수 있어야 한다.
         */
        return memberRepository.searchMember(email, name,
                PageRequest.of(page, size, Sort.by("createdAt").descending()));
    }
}
