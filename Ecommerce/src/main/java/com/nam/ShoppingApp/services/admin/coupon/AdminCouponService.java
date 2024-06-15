package com.nam.ShoppingApp.services.admin.coupon;

import com.nam.ShoppingApp.entity.Coupon;

import java.util.List;

public interface AdminCouponService {
    Coupon createCoupon(Coupon coupon);
    List<Coupon> getAllCoupons();
}
