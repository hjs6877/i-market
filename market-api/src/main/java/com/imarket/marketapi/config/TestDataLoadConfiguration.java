package com.imarket.marketapi.config;

import com.imarket.marketdomain.domain.*;
import com.imarket.marketdomain.repository.member.MemberRepository;
import com.imarket.marketdomain.repository.product_category.ProductCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@Profile("local")
public class TestDataLoadConfiguration {
    private ProductCategoryRepository productCategoryRepository;
    private MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner dataLoader(MemberRepository memberRepository,
                                        ProductCategoryRepository productCategoryRepository,
                                        PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.passwordEncoder = passwordEncoder;

        return (String... args) -> {
            log.info("# Initialize Sample Data.");
            createSampleData();
        };
    }

    private void createSampleData() {
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

        Member member1 = new Member("seller1@imarket.com",
                passwordEncoder.encode("Hjs1234!"), "Tom", "010-1234-1111", Member.Gender.MALE);
        Seller seller1 = new Seller();
        Buyer buyer1 = new Buyer();
        Product product1 = new Product("LG 냉장고", 2000000);
        Product product2 = new Product("삼성 TV", 1000000);
        Payment payment1 = new Payment("D뱅크", passwordEncoder.encode("1234-5678-1234-5678"));
        member1.getRoles().add("USER");
        member1.setSeller(seller1);
        member1.setBuyer(buyer1);
        buyer1.addPayment(payment1);
        seller1.addProduct(product1);
        seller1.addProduct(product2);
        product1.setProductCategory(productCategory1);
        product2.setProductCategory(productCategory1);

        Member member2 = new Member("buyer1@imarket.com",
                passwordEncoder.encode("Hjs1234!"), "David", "010-1111-1111", Member.Gender.MALE);
        Seller seller2 = new Seller();
        Buyer buyer2 = new Buyer();
        Payment payment2 = new Payment("A뱅크", passwordEncoder.encode("1111-2222-3333-4444"));
        member2.getRoles().add("USER");
        member2.setSeller(seller2);
        member2.setBuyer(buyer2);
        buyer1.addPayment(payment2);

        Member member3 = new Member("buyer2@imarket.com",
                passwordEncoder.encode("Hjs1234!"), "Jane", "010-2222-2222", Member.Gender.FEMALE);
        Seller seller3 = new Seller();
        Buyer buyer3 = new Buyer();
        Payment payment3 = new Payment("B뱅크", passwordEncoder.encode("5555-5555-5555-5555"));
        member3.getRoles().add("USER");
        member3.setSeller(seller3);
        member3.setBuyer(buyer3);
        buyer1.addPayment(payment3);


        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
    }
}
