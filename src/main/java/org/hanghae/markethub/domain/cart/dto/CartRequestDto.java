package org.hanghae.markethub.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hanghae.markethub.domain.item.entity.Item;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CartRequestDto {
    private List<Long> itemId;
    private List<Integer> quantity;
    private String cartIp;
}
