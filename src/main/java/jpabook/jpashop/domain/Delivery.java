package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)      /*  주로 어디에 연관관계의 주인을 할까? 자주 조회가 일어나는 곳에서! */
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)    /* EnumType은 ORDER 절대 하지마! STRING 으로 해야한다. */
    private DeliveryStatus status; // READY, COMP

}
