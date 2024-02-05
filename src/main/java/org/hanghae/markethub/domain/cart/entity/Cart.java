package org.hanghae.markethub.domain.cart.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hanghae.markethub.domain.cart.dto.CartRequestDto;
import org.hanghae.markethub.domain.item.entity.Item;
import org.hanghae.markethub.domain.user.entity.User;

import org.hanghae.markethub.global.constant.Status;
import org.hanghae.markethub.global.date.BaseTimeEntity;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "cart")
@AllArgsConstructor
@Builder
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;


    public void update(CartRequestDto requestDto){
        for (int i = 0; i < requestDto.getItem().size(); i++){
            this.item = requestDto.getItem().get(i);
            this.quantity = requestDto.getQuantity().get(i);
        }
    }

    public void delete() {
        this.status = Status.DELETED;
    }
}
