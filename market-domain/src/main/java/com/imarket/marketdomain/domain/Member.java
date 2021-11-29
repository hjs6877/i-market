package com.imarket.marketdomain.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private long id;

    @Column(nullable = false, unique=true)
    private String userId;

    @Column(nullable = false)
    private String password;

//    @Field
//    @SortableField
//    @Analyzer(definition = "koreanAnalyzer")
    @Column(nullable = false)
    private String name;
    private String nickName;
    private String phone;
    private Gender gender;

    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
    private Buyer buyer;

    @OneToOne(mappedBy = "member", cascade = CascadeType.PERSIST)
    private Seller seller;

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
        if (buyer.getMember() != this) {
            buyer.setMember(this);
        }
    }

    public enum Gender {
        MALE,
        FEMALE
    }
}


