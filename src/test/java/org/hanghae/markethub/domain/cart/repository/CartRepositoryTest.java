package org.hanghae.markethub.domain.cart.repository;

import jakarta.transaction.Transactional;
import org.hanghae.markethub.domain.cart.entity.Cart;
import org.hanghae.markethub.domain.item.entity.Item;
import org.hanghae.markethub.domain.item.repository.ItemRepository;
import org.hanghae.markethub.domain.store.entity.Store;
import org.hanghae.markethub.domain.store.repository.StoreRepository;
import org.hanghae.markethub.domain.user.entity.User;
import org.hanghae.markethub.global.constant.Role;
import org.hanghae.markethub.global.constant.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class CartRepositoryTest {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private Item item;

    @BeforeEach
    public void cartBuilder(){

        User user1 = User.builder()
                .email("1234@naver.com")
                .password("1234")
                .name("lee")
                .phone("010-1234")
                .address("서울시")
                .role(Role.ADMIN)
                .status(Status.EXIST)
                .build();

        user = userRepository.save(user1);

        Store store = Store.builder()
                .user(user)
                .status(Status.EXIST)
                .build();

        Item item1 = Item.builder()
                .itemName("노트북")
                .price(500000)
                .quantity(5)
                .user(user1)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.EXIST)
                .store(store)
                .build();

        storeRepository.save(store);
        item = itemRepository.save(item1);

    }


    @Test
    public void CartRepository가Null이아님() {
        assertThat(cartRepository).isNotNull();
    }

    @Test
    @DisplayName("Cart등록")
    public void addCart(){

        // given
        Cart cart = Cart.builder()
                .user(user)
                .item(item)
                .price(11111)
                .quantity(11)
                .address("address")
                .status(Status.EXIST)
                .build();

        // when
        Cart save = cartRepository.save(cart);

        // then
        assertThat(save.getPrice()).isEqualTo(11111);
        assertThat(save.getQuantity()).isEqualTo(11);
        assertThat(save.getItem()).isEqualTo(item);

    }
}