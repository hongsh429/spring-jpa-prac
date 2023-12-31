package jpabook.jpashop.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) /* ✨✨ */
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")      /* 명확하게 어디의 아이디인지 명시하는게 좋음 */
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

                                /*cascade(영속성 전이): 부모테이블에서 자식테이블을 한번에 저장/삭제하는 것 */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)    /* EnumType은 ORDER 절대 하지마! STRING 으로 해야한다. */
    private OrderStatus status;  //주문상태 ORDER , CANCEL

    // == 연관관계 메서드 == //
    /*
    연관 관계 편의 메서드는 연관관계의 주인인 쪽에서 정의하는 것이 좋다.
        왜? 우리가 저장을 할 때, 보통 연관관계 주인 쪽에서 정의를 할텐데,
            이때 연관관계의 주인이 아닌 쪽에 세팅을 해주는 메서드가 있으면 좋기 때문에!
    */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.member = member;
        order.delivery = delivery;
        for (OrderItem orderItem : orderItems) {
            order.orderItems.add(orderItem);
        }
        order.status = OrderStatus.ORDER;
        order.orderDate = LocalDateTime.now();
        return order;
    }

    //==비지니스 로직==//

    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.status = OrderStatus.CANCEL;
        for (OrderItem orderItem : this.orderItems) {
            // 해당 관련 entity에서 cancel 메서드 발생
            orderItem.cancel();
        }
    }


    //== 조회 로직 ==//
    /**
     * 전체 주문 가격 조회
     * */
    public int getTotalPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
