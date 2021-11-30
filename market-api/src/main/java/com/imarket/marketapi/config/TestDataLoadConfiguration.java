package com.imarket.marketapi.config;

import com.imarket.marketdomain.domain.*;
import com.imarket.marketdomain.repository.MemberRepository;
import com.imarket.marketdomain.repository.ProductCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
@Profile("local")
public class TestDataLoadConfiguration {
    private ProductCategoryRepository productCategoryRepository;
    private MemberRepository memberRepository;

    @Bean
    public CommandLineRunner dataLoader(MemberRepository memberRepository,
                                        ProductCategoryRepository productCategoryRepository) {
        this.memberRepository = memberRepository;
        this.productCategoryRepository = productCategoryRepository;

        return (String... args) -> {
            log.info("# Initialize Sample Data.");
            createSampleData();
        };
    }

    private void createSampleData() {
        // 상품 카테고리 등록
        ProductCategory productCategory1 = new ProductCategory("가전기기");
        ProductCategory productCategory2 = new ProductCategory("의류");
        ProductCategory productCategory3 = new ProductCategory("휴대폰/전자기기");
        ProductCategory productCategory4 = new ProductCategory("컴퓨터/통신");
        ProductCategory productCategory5 = new ProductCategory("자동차");
        ProductCategory productCategory6 = new ProductCategory("신발");
        ProductCategory productCategory7 = new ProductCategory("스포츠");
        ProductCategory productCategory8 = new ProductCategory("기타");
        productCategoryRepository.save(productCategory1);
        productCategoryRepository.save(productCategory2);
        productCategoryRepository.save(productCategory3);
        productCategoryRepository.save(productCategory4);
        productCategoryRepository.save(productCategory5);
        productCategoryRepository.save(productCategory6);
        productCategoryRepository.save(productCategory7);
        productCategoryRepository.save(productCategory8);

        Member member1 = new Member("aaa@imarket.com",
                "Hjs1234!", "Tom", "010-1234-1111", Member.Gender.MALE);
        Seller seller1 = new Seller();
        Buyer buyer1 = new Buyer();
        member1.setSeller(seller1);
        member1.setBuyer(buyer1);
        Product product1 = new Product("LG 냉장고", 200000);
        seller1.addProduct(product1);
        product1.setProductCategory(productCategory1);

        memberRepository.save(member1);
    }
}
