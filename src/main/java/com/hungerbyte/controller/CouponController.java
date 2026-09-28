package com.hungerbyte.controller;

import com.hungerbyte.dto.ApiResponse;
import com.hungerbyte.entity.Coupon;
import com.hungerbyte.exception.ResourceNotFoundException;
import com.hungerbyte.repository.CouponRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@CrossOrigin(origins = "*")
public class CouponController {

    private final CouponRepository couponRepository;

    public CouponController(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Coupon>>> getCoupons() {
        return ResponseEntity.ok(ApiResponse.ok("Coupons retrieved", couponRepository.findByIsActiveTrue()));
    }

    @GetMapping("/validate/{code}")
    public ResponseEntity<ApiResponse<Coupon>> validateCoupon(
            @PathVariable String code,
            @RequestParam(defaultValue = "0") Double amount) {
        Coupon coupon = couponRepository.findByCodeIgnoreCaseAndIsActiveTrue(code)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid or expired coupon code: " + code));

        if (coupon.getMinOrderAmount() != null && amount < coupon.getMinOrderAmount()) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Minimum order value for " + code + " is ₹" + coupon.getMinOrderAmount()));
        }

        return ResponseEntity.ok(ApiResponse.ok("Coupon is valid!", coupon));
    }
}
