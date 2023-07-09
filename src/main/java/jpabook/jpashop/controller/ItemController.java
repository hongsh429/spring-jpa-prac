package jpabook.jpashop.controller;


import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();
        /* 원래대로라면 setter 로 하는 것 보다, static 메서드 또는 생성자로 하는 것이 좋다. */
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        Book item = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    /**
     * 상품 수정
     */
    @PostMapping(value = "/items/{itemId}/edit")
    @Transactional
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form) {

        /*
        merge : 영속성 컨텍스트에는 없으나, 나의 디비에는 있는 것을 가지고 와서 변경을 한다.
            주의 사항!!!
                변경하고 싶지 않은 내용을 담지 않았다면, 그 필드를 null로 처리해버린다.!... 모든 필드를 변경하기 때문에...✨✨
                그래서 보통은 entity를 변경할 때, dto를 통해서 변경하고 싶은 것만 변경하기 때문에 merge를 사용하는 것은 위험하다.
        */
        /* merge 사용 */
//        Book book = new Book();
//        book.setId(form.getId());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setAuthor(form.getAuthor());
//        book.setIsbn(form.getIsbn());
//        itemService.saveItem(book);

        /* 변경 감지 사용 */
        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity());
        return "redirect:/items";
    }
}
