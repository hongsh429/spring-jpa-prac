package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/* 값 타입 : Embedded 타입 */
@Embeddable
@Getter @Setter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address() {
        // 기본 생성자는 reflection , proxy 때문이라고요?
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
