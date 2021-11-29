package com.imarket.marketdomain.domain;

import lombok.Data;

import javax.persistence.*;

@Data

@Entity
public class Buyer extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        if (member.getBuyer() != this) {
            member.setBuyer(this);
        }
    }
}
