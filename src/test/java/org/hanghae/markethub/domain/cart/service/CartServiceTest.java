package org.hanghae.markethub.domain.cart.service;

import org.hanghae.markethub.domain.cart.dto.CartRequestDto;
import org.hanghae.markethub.domain.cart.dto.CartResponseDto;
import org.hanghae.markethub.domain.cart.entity.Cart;
import org.hanghae.markethub.domain.cart.repository.CartRepository;
import org.hanghae.markethub.domain.item.entity.Item;
import org.hanghae.markethub.domain.store.entity.Store;
import org.hanghae.markethub.domain.user.entity.User;
import org.hanghae.markethub.global.constant.Role;
import org.hanghae.markethub.global.constant.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @InjectMocks
    private CartService cartService;
    private Item item;
    private Item item2;
    private Item item3;
    private Item notExistItem;
    private Item soldOutItem;

    @BeforeEach
    public void init() {

        User user = User.builder()
                .id(1L)
                .email("1234@naver.com")
                .password("1234")
                .name("lee")
                .phone("010-1234")
                .address("서울시")
                .role(Role.ADMIN)
                .status(Status.EXIST).build();

        Store store = Store.builder()
                .id(1L)
                .user(user)
                .status(Status.EXIST)
                .build();

        item = Item.builder()
                .id(2L)
                .itemName("노트북2")
                .price(5000)
                .quantity(5)
                .user(user)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.EXIST)
                .store(store)
                .build();

        item2 = Item.builder()
                .id(3L)
                .itemName("노트북3")
                .price(500)
                .quantity(5)
                .user(user)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.EXIST)
                .store(store)
                .build();

        item3 = Item.builder()
                .id(1L)
                .itemName("노트북")
                .price(500000)
                .quantity(5)
                .user(user)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.EXIST)
                .store(store)
                .build();

        notExistItem = Item.builder()
                .id(1L)
                .itemName("노트북")
                .price(500000)
                .quantity(5)
                .user(user)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.DELETED)
                .store(store)
                .build();

        soldOutItem = Item.builder()
                .id(1L)
                .itemName("노트북")
                .price(500000)
                .quantity(0)
                .user(user)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.EXIST)
                .store(store)
                .build();
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

            User user = User.builder()
                    .build();

            for (int i = 0; i < items.size(); i++) {
                Optional<Cart> checkCart = cartRepository.findByitemId(items.get(i).getId());
                if (checkCart.isPresent()) {
                    checkCart.get().update(requestDto);
                    when(cartRepository.save(any())).thenReturn(checkCart.get());
                } else {
                    Cart cart = Cart.builder()
                            .item(items.get(i))
                            .status(Status.EXIST)
                            .address(user.getAddress())
                            .quantity(requestDto.getQuantity().get(i))
                            .price(items.get(i).getPrice() * requestDto.getQuantity().get(i))
                            .user(user)
                            .build();

                    when(cartRepository.save(any())).thenReturn(cart);
                }
            }


            // when
            cartService.addCart(user, requestDto);

            // then
            verify(cartRepository, times(3)).save(any(Cart.class));
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


        CartRequestDto requestDto = CartRequestDto.builder()
                .item(items)
                .quantity(quantities)
                .build();

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.addCart(User.builder().build(), requestDto);
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
        CartRequestDto requestDto = CartRequestDto.builder()
                .item(items)
                .quantity(quantities)
                .build();


        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            cartService.addCart(User.builder().build(), requestDto);
        });
    }


    @Nested
    class updateCart {
        @Test
        @DisplayName("수정 성공")
        void updateCartSuccess() {
            // given
            User user = User.builder()
                    .build();

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

            CartRequestDto req = CartRequestDto.builder()
                    .item(items)
                    .quantity(quantities)
                    .build();

            given(cartRepository.findById(anyLong())).willReturn(Optional.of(setCart));

            // when
            cartService.updateCart(user, req, setCart.getCartId());

            // then
            assertThat(setCart.getQuantity()).isEqualTo(3);
        }
    }

    @Nested
    class deleteCart {
        @Test
        @DisplayName("삭제 성공")
        void deleteCartSuccess() {

            // given
            User user = User.builder()
                    .build();

            Cart setCart = Cart.builder()
                    .cartId(1L)
                    .item(item)
                    .status(Status.EXIST)
                    .address(user.getAddress())
                    .quantity(1)
                    .price(1)
                    .user(user).build();

            given(cartRepository.findById(anyLong())).willReturn(Optional.of(setCart));

            // when
            cartService.deleteCart(user, 1L);

            // then
            assertThat(setCart.getStatus()).isEqualTo(Status.DELETED);
        }
    }

    @Nested
    class getCarts {
        @Test
        @DisplayName("전체 조회 성공")
        void getCartsSuccess() {

            // given
            User user = User.builder()
                    .build();

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

            List<Cart> carts = new ArrayList<>();
            carts.add(setCart);
            carts.add(setCart1);
            carts.add(setCart2);

            given(cartRepository.findAllByUser(user)).willReturn(carts);

            // when
            List<CartResponseDto> cartsRes = cartService.getCarts(user);

            // then
            assertThat(cartsRes.size()).isEqualTo(3);
        }
    }
}