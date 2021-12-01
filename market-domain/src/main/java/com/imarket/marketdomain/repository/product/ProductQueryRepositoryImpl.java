package com.imarket.marketdomain.repository.product;

import com.imarket.marketdomain.domain.Product;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static com.imarket.marketdomain.domain.QProduct.product;

public class ProductQueryRepositoryImpl implements ProductQueryRepository {
    private final JPAQueryFactory queryFactory;

    @Autowired
    public ProductQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    // TODO Refactoring
    @Override
    public Page<Product> searchProduct(String productName, String description, Pageable pageable) {
        BooleanBuilder builder = getSearchCondition(productName, description);
        QueryResults<Product> results = queryFactory.selectFrom(product)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.createdAt.desc())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // TODO Refactoring
    private BooleanBuilder getSearchCondition(String productName, String description) {
        BooleanBuilder builder = new BooleanBuilder();
        if (StringUtils.isNotEmpty(productName)) {
            builder.and(product.productName.contains(productName));
        }
        if (StringUtils.isNotEmpty(description)) {
            builder.and(product.description.contains(description));
        }
        return builder;
    }
}
