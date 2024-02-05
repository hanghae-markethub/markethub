package org.hanghae.markethub.domain.purchase.controller;

import lombok.RequiredArgsConstructor;
import org.hanghae.markethub.domain.purchase.dto.PurchaseRequestDto;
import org.hanghae.markethub.domain.purchase.dto.PurchaseResponseDto;
import org.hanghae.markethub.domain.purchase.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/purchase")
public class PurchaseController {


    private final PurchaseService purchaseService;


    @PostMapping("/{userId}")
    public ResponseEntity<String> createPurchase(@PathVariable String userId, @RequestBody PurchaseRequestDto purchaseRequestDto) {
        try {
            PurchaseResponseDto purchaseResponseDto = purchaseService.createOrder(purchaseRequestDto, userId);
            // 정상적으로 처리되었을 때 200 OK 반환
            return ResponseEntity.ok("Purchase created successfully.");
        } catch (Exception e) {
            // 예외 발생 시 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating purchase: " + e.getMessage());
        }
    }

    @PostMapping("/singleBuy")
    public ResponseEntity<String> createSinglePurchase(@RequestBody PurchaseRequestDto.SinglePurchaseRequestDto singlePurchaseRequestDto) {
        try {
            PurchaseResponseDto purchaseResponseDto = purchaseService.createSingleOrder(singlePurchaseRequestDto);
            return ResponseEntity.ok("주문 등록 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage());
        }

    }

    @GetMapping("/search/single/{email}")
    public ResponseEntity<PurchaseResponseDto> findPurchaseByEmail(@PathVariable String email) {

        PurchaseResponseDto purchaseResponseDto = purchaseService.findPurchaseByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(purchaseResponseDto);

    }

    @GetMapping("/search/{email}")
        public ResponseEntity<List<PurchaseResponseDto>> findAllPurchaseByEmail(@PathVariable String email) {
            List<PurchaseResponseDto> responseDtoList = purchaseService.findAllPurchaseByEmail(email);

            return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
        }


}

