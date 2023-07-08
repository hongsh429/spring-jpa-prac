package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // == 비즈니스 로직== //
    /*
    데이터를 가지고 있는 entity에서 비즈니스로직을 세팅해 놓으면
     1. 응집도 측면에서,
     2. 관리 측면에서 좋다
     3. setter를 가지고 하는 것보다 이렇게 별도의 메서드로 setter의 역할을 하는 로직을 만드는 것이 좋다
    */

    /**
     * stock 증가 로직
     * @param stockQuantity
     */
    public void addStock(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }


    /**
     * stock 감소 로직
     * @param stockQuantity
     */
    public void removeStock(int stockQuantity) throws NotEnoughStockException {
        int restStock = this.stockQuantity - stockQuantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
