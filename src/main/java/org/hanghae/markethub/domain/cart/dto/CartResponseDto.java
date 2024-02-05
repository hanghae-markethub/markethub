package org.hanghae.markethub.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hanghae.markethub.domain.item.entity.Item;

@Getter
@Builder
@AllArgsConstructor
public class CartResponseDto {
    private Item item;
    private int price;
    private int quantity;
}
