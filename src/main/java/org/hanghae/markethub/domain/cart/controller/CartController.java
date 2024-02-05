package org.hanghae.markethub.domain.cart.controller;

import lombok.RequiredArgsConstructor;
import org.hanghae.markethub.domain.cart.dto.CartRequestDto;
import org.hanghae.markethub.domain.cart.dto.CartResponseDto;
import org.hanghae.markethub.domain.cart.entity.Cart;
import org.hanghae.markethub.domain.cart.service.CartService;
import org.hanghae.markethub.domain.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 향후 Controller로 변경
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public List<CartResponseDto> getCarts(User user){
         return cartService.getCarts(user);
    }

    @PostMapping
    public ResponseEntity<String> addCart(User user, CartRequestDto requestDto){
        return cartService.addCart(user, requestDto);
    }

    @PatchMapping("/{cartId}")
    public ResponseEntity<String> updateCart(User user, CartRequestDto requestDto,@PathVariable Long cartId){
        return cartService.updateCart(user,requestDto,cartId);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> deleteCart(User user, @PathVariable Long cartId){
        return cartService.deleteCart(user,cartId);
    }

}
