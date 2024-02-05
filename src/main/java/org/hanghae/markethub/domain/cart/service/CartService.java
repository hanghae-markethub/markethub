package org.hanghae.markethub.domain.cart.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.hanghae.markethub.domain.cart.dto.CartRequestDto;
import org.hanghae.markethub.domain.cart.dto.CartResponseDto;
import org.hanghae.markethub.domain.cart.entity.Cart;
import org.hanghae.markethub.domain.cart.repository.CartRepository;
import org.hanghae.markethub.domain.item.entity.Item;
import org.hanghae.markethub.domain.user.entity.User;
import org.hanghae.markethub.global.constant.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    // security 도입되면 User변경 해야함
    // 다른 부분을 연결하면 item도 존재하는지 검사넣기
    public ResponseEntity<String> addCart(User user, CartRequestDto requestDto){

        List<Item> items = requestDto.getItem();
        ValidItems(items);

        for (int i = 0; i < items.size(); i++){
            Optional<Cart> checkCart = cartRepository.findByitemId(items.get(i).getId());
            if (checkCart.isPresent()) {
                checkCart.get().update(requestDto);
                cartRepository.save(checkCart.get());
            } else {
                Cart cart = Cart.builder()
                        .item(items.get(i))
                        .status(Status.EXIST)
                        .address(user.getAddress())
                        .quantity(requestDto.getQuantity().get(i))
                        .price(items.get(i).getPrice() * requestDto.getQuantity().get(i))
                        .user(user)
                        .build();

                cartRepository.save(cart);
            }
        }

        return ResponseEntity.ok("Success Cart");
    }

    @Transactional
    public ResponseEntity<String> updateCart(User user, CartRequestDto requestDto,Long cartId) {

        ValidItems(requestDto.getItem());

        Cart cart = cartRepository.findById(cartId).orElseThrow(null);
        cart.update(requestDto);

        return ResponseEntity.ok("Success Update Cart");
    }

    public ResponseEntity<String> deleteCart(User user,Long cartId){

        Cart cart = cartRepository.findById(cartId).orElseThrow(null);
        cart.delete();

        return ResponseEntity.ok("Success Delete Cart");
    }

    public List<CartResponseDto> getCarts(User user){

        try{
            return cartRepository.findAllByUser(user).stream()
                    .map(cart -> CartResponseDto.builder()
                            .price(cart.getPrice())
                            .item(cart.getItem())
                            .quantity(cart.getQuantity())
                            .build())
                    .collect(Collectors.toList());
        }catch (Exception e){
            throw new NullPointerException("해당 user의 장바구니에는 아무것도 없습니다");
        }
    }


    private static void ValidItems(List<Item> items) {
        for (Item item : items) {
            if (item.getStatus().equals(Status.DELETED) || item.getQuantity() <= 0){
                throw new IllegalArgumentException("해당 상품은 존재하지않으므로 다시 확인해주세요");
            }
        }
    }

}
