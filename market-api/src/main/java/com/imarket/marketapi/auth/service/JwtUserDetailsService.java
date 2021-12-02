package com.imarket.marketapi.auth.service;

import com.imarket.marketdomain.domain.Buyer;
import com.imarket.marketdomain.domain.Member;
import com.imarket.marketdomain.domain.Seller;
import com.imarket.marketdomain.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;

    @Autowired
    public JwtUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElse(null);

        List<GrantedAuthority> roles = new ArrayList<>();

        if (member == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        for (String role : member.getRoles()) {
            if (role.equals("ADMIN")) {
                roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                roles.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
        }

        MarketUser marketUser = new MarketUser(username, member.getPassword(), roles);

        Seller seller = member.getSeller();
        Buyer buyer = member.getBuyer();
        Long memberId = member.getMemberId();
        Optional.ofNullable(memberId).ifPresent(id -> marketUser.setMemberId(id));
        Optional.ofNullable(seller).ifPresent(seller1 -> marketUser.setSellerId(seller1.getSellerId()));
        Optional.ofNullable(buyer).ifPresent(buyer1 -> marketUser.setBuyerId(buyer1.getBuyerId()));

        return marketUser;
    }
}
