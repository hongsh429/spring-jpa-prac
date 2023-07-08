package jpabook.jpashop.repository;


import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { // 새로생성하는 객체. & 영속성 컨텍스트에도 없어
            em.persist(item);
        } else {
            em.merge(item);     // 디비에 등록된건 가지고 온거야. & 영속성 컨텍스트에 있어. 일종의 update
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em
                .createQuery("select i from Item i", Item.class)
                .getResultList();
    }


}
