package com.imarket.marketdomain.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long id;

    @Column(nullable = false, unique=true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Transient
    private String verifiedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

//    @Field
//    @SortableField
//    @Analyzer(definition = "koreanAnalyzer")
    @Column(nullable = false)
    private String name;
    private String nickName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private Gender gender;

    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
    private Buyer buyer;

    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
    private Seller seller;

    public Member(){}

    public Member(String email, String password, String name, String phone, Gender gender) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
        if (buyer.getMember() != this) {
            buyer.setMember(this);
        }
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
        if (seller.getMember() != this) {
            seller.setMember(this);
        }
    }

    public enum Gender {
        MALE,
        FEMALE
    }
}


