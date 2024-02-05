package org.hanghae.markethub.domain.cart.service;

import jakarta.transaction.Transactional;
import org.hanghae.markethub.domain.cart.dto.CartRequestDto;
import org.hanghae.markethub.domain.cart.entity.Cart;
import org.hanghae.markethub.domain.cart.repository.CartRepository;
import org.hanghae.markethub.domain.item.entity.Item;
import org.hanghae.markethub.domain.item.repository.ItemRepository;
import org.hanghae.markethub.domain.store.entity.Store;
import org.hanghae.markethub.domain.store.repository.StoreRepository;
import org.hanghae.markethub.domain.user.entity.User;
import org.hanghae.markethub.global.constant.Role;
import org.hanghae.markethub.global.constant.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
class CartServiceTest1 {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    private User user;
    private Item item;
    private Item item2;
    private Item item3;
    private Item notExistItem;
    private Item soldOutItem;

    @BeforeEach
    public void datas() {

        User user1 = User.builder()
                .id(1L)
                .email("1234@naver.com")
                .password("1234")
                .name("lee")
                .phone("010-1234")
                .address("서울시")
                .role(Role.ADMIN)
                .status(Status.EXIST).build();

        user = userRepository.save(user1);

        Store store = Store.builder()
                .id(1L)
                .user(user)
                .status(Status.EXIST)
                .build();

        Item item1 = Item.builder()
                .id(1L)
                .itemName("노트북")
                .price(500000)
                .quantity(5)
                .user(user1)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.EXIST)
                .store(store)
                .build();

        Item item22 = Item.builder()
                .id(2L)
                .itemName("노트북2")
                .price(5000)
                .quantity(5)
                .user(user1)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.EXIST)
                .store(store)
                .build();

        Item item33 = Item.builder()
                .id(3L)
                .itemName("노트북3")
                .price(500)
                .quantity(5)
                .user(user1)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.EXIST)
                .store(store)
                .build();

        Item deleteItem = Item.builder()
                .id(1L)
                .itemName("노트북")
                .price(500000)
                .quantity(5)
                .user(user1)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.DELETED)
                .store(store)
                .build();

        Item soldOut = Item.builder()
                .id(1L)
                .itemName("노트북")
                .price(500000)
                .quantity(0)
                .user(user1)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.EXIST)
                .store(store)
                .build();

        storeRepository.save(store);
        item = itemRepository.save(item1);
        item2 = itemRepository.save(item22);
        item3 = itemRepository.save(item33);
        notExistItem = itemRepository.save(deleteItem);
        soldOutItem = itemRepository.save(soldOut);
    }

    @Nested
    class addCart {
        @Test
        @DisplayName("카드 등록 성공")
        void addCartSuccess() {
            // given
            List<Item> items = new ArrayList<>();
            items.add(item);
            items.add(item2);
            items.add(item3);

            List<Integer> quantities = new ArrayList<>();
            quantities.add(1);
            quantities.add(2);
            quantities.add(3);


            CartRequestDto requestDto = CartRequestDto.builder()
                    .item(items)
                    .quantity(quantities)
                    .build();

            // when
            List<Cart> carts = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                Optional<Cart> checkCart = cartRepository.findByitemId(items.get(i).getId());
                if (checkCart.isPresent()) {
                    checkCart.get().update(requestDto);
                    Cart save = cartRepository.save(checkCart.get());
                    carts.add(save);
                } else {
                    Cart cart = Cart.builder()
                            .item(items.get(i))
                            .status(Status.EXIST)
                            .address(user.getAddress())
                            .quantity(requestDto.getQuantity().get(i))
                            .price(items.get(i).getPrice() * requestDto.getQuantity().get(i))
                            .user(user)
                            .build();

                    Cart save = cartRepository.save(cart);
                    carts.add(save);
                }
            }

            // then
            assertThat(carts.size()).isEqualTo(3);

        }
    }

    @Test
    @DisplayName("상품 삭제되서 장바구니 추가안됨")
    void notExistItemFail() {
        // given
        List<Item> items = new ArrayList<>();
        items.add(notExistItem);

        List<Integer> quantities = new ArrayList<>();
        quantities.add(1);

        CartRequestDto.builder()
                .item(items)
                .quantity(quantities)
                .build();

        // when
        if (item.getStatus().equals(Status.DELETED) || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("해당 상품은 존재하지않으므로 다시 확인해주세요");
        }

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("해당 상품은 존재하지않으므로 다시 확인해주세요");
        });
    }

    @Test
    @DisplayName("상품 개수가 없어서 장바구니 추가안됨")
    void soldOutItemFail() {
        // given
        List<Item> items = new ArrayList<>();
        items.add(soldOutItem);

        List<Integer> quantities = new ArrayList<>();
        quantities.add(1);

        CartRequestDto.builder()
                .item(items)
                .quantity(quantities)
                .build();


        // when
        if (item.getStatus().equals(Status.DELETED) || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("해당 상품은 존재하지않으므로 다시 확인해주세요");
        }

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("해당 상품은 존재하지않으므로 다시 확인해주세요");
        });
    }


    @Nested
    class updateCart {
        @Test
        @DisplayName("수정 성공")
        void updateCartSuccess() {
            // given

            List<Item> items = new ArrayList<>();
            items.add(item);

            List<Integer> quantities = new ArrayList<>();
            quantities.add(3);

            Cart setCart = Cart.builder()
                    .cartId(1L)
                    .item(item)
                    .status(Status.EXIST)
                    .address(user.getAddress())
                    .quantity(1)
                    .price(1)
                    .user(user).build();

            CartRequestDto res = CartRequestDto.builder()
                    .item(items)
                    .quantity(quantities)
                    .build();

            cartRepository.save(setCart);

            // when
            Cart cart = cartRepository.findById(setCart.getCartId()).orElseThrow(null);
            cart.update(res);

            // then
            assertThat(cart.getQuantity()).isEqualTo(3);
        }
    }


    @Nested
    class deleteCart {
        @Test
        @DisplayName("삭제 성공")
        void deleteCartSuccess() {
            // given
            Cart setCart = Cart.builder()
                    .cartId(1L)
                    .item(item)
                    .status(Status.EXIST)
                    .address(user.getAddress())
                    .quantity(1)
                    .price(1)
                    .user(user).build();

            cartRepository.save(setCart);

            // when
            Cart cart = cartRepository.findById(setCart.getCartId()).orElseThrow(null);
            cart.delete();

            // then
            assertThat(cart.getStatus()).isEqualTo(Status.DELETED);
        }
    }


    @Nested
    class getCarts {
        @Test
        @DisplayName("전체 조회 성공")
        void getCartsSuccess() {
            // given
            Cart setCart = Cart.builder()
                    .cartId(1L)
                    .item(item)
                    .status(Status.EXIST)
                    .address(user.getAddress())
                    .quantity(1)
                    .price(1)
                    .user(user).build();

            Cart setCart1 = Cart.builder()
                    .cartId(2L)
                    .item(item)
                    .status(Status.EXIST)
                    .address(user.getAddress())
                    .quantity(2)
                    .price(2)
                    .user(user).build();

            Cart setCart2 = Cart.builder()
                    .cartId(3L)
                    .item(item)
                    .status(Status.EXIST)
                    .address(user.getAddress())
                    .quantity(3)
                    .price(3)
                    .user(user).build();

            cartRepository.save(setCart);
            cartRepository.save(setCart1);
            cartRepository.save(setCart2);

            // when
            List<Cart> carts = cartRepository.findAllByUser(user);

            // then
            assertThat(carts.size()).isEqualTo(3);
        }
    }
}


