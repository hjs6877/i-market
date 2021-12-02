package com.imarket.marketdomain.repository.member;

import com.imarket.marketdomain.domain.Member;
import com.imarket.marketdomain.domain.Product;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryFactory;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.imarket.marketdomain.domain.QMember.member;
import static com.imarket.marketdomain.domain.QBuyer.buyer;

public class MemberQueryRepositoryImpl implements MemberQueryRepository {
    private JPAQueryFactory queryFactory;

    @Autowired
    public MemberQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    // TODO Refactoring
    @Override
    public Page<Member> searchMember(String email, String name, Pageable pageable) {
        BooleanBuilder builder = getSearchCondition(email, name);
        QueryResults<Member> results = queryFactory.selectFrom(member)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(member.createdAt.desc())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // TODO Refactoring
    private BooleanBuilder getSearchCondition(String email, String name) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotEmpty(email)) {
            builder.and(member.email.contains(email));
        }
        if (StringUtils.isNotEmpty(name)) {
            builder.and(member.name.contains(name));
        }
        return builder;
    }
}
