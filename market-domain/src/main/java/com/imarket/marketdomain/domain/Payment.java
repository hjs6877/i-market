package com.imarket.marketdomain.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = {"buyer"})
@Entity
public class Payment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long paymentId;

    @Column(name = "card_nick_name", nullable = false)
    private String cardNickName;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    public void setBuyer(Buyer buyer) {
        if (this.buyer != null) {
            this.buyer.getPayments().remove(this);
        }

        this.buyer = buyer;

        if (!this.buyer.getPayments().contains(this)) {
            this.buyer.getPayments().add(this);
        }
    }
}
