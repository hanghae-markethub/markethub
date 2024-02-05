package org.hanghae.markethub.domain.cart.entity;

import org.assertj.core.api.Assertions;
import org.hanghae.markethub.domain.item.entity.Item;
import org.hanghae.markethub.domain.store.entity.Store;
import org.hanghae.markethub.domain.user.entity.User;
import org.hanghae.markethub.global.constant.Role;
import org.hanghae.markethub.global.constant.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    @DisplayName("table builder test")
    void tableBuilder(){
        // given

        User user = User.builder()
                .id(1L)
                .email("1234@naver.com")
                .password("1234")
                .name("lee")
                .phone("010-1234")
                .address("서울시")
                .role(Role.ADMIN)
                .status(Status.EXIST)
                .build();

        Store store = Store.builder()
                .id(1L)
                .user(user)
                .status(Status.EXIST)
                .build();

        Item item = Item.builder()
                .itemName("노트북")
                .price(500000)
                .quantity(5)
                .itemInfo("구형 노트북")
                .category("가전 제품")
                .status(Status.EXIST)
                .store(store)
                .build();

        Cart cart = Cart.builder()
                .cartId(1L)
                .user(user)
                .item(item)
                .price(11111)
                .quantity(11)
                .address("address")
                .status(Status.EXIST)
                .build();

        Assertions.assertThat(cart.getPrice()).isEqualTo(11111);

    }

}