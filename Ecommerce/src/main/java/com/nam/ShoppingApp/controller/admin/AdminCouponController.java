package com.nam.ShoppingApp.controller.admin;

import com.nam.ShoppingApp.dto.ApiResponse;
import com.nam.ShoppingApp.dto.CouponDto;
import com.nam.ShoppingApp.entity.Coupon;
import com.nam.ShoppingApp.exception.AppException;
import com.nam.ShoppingApp.exceptions.ValidationException;
import com.nam.ShoppingApp.services.admin.coupon.AdminCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    @Autowired private AdminCouponService adminCouponService;

    @PostMapping
    public ApiResponse<Coupon> createCoupon(@RequestBody Coupon coupon) {
        ApiResponse<Coupon> response = new ApiResponse<>();

        response.setResult(adminCouponService.createCoupon(coupon));

        return response;
    }

    @GetMapping
    public ResponseEntity<?> getAllCoupons() {
        return ResponseEntity.ok(adminCouponService.getAllCoupons());
    }
}
