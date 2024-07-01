package com.nam.ShoppingApp.services.admin.coupon;

import com.nam.ShoppingApp.entity.Coupon;
import com.nam.ShoppingApp.exception.AppException;
import com.nam.ShoppingApp.exception.ErrorCode;
import com.nam.ShoppingApp.exceptions.ValidationException;
import com.nam.ShoppingApp.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCouponServiceImpl implements AdminCouponService {

  @Autowired private CouponRepository couponRepository;

  public Coupon createCoupon(Coupon coupon) {
    if (couponRepository.existsByCode(coupon.getCode())) {
      throw new AppException(ErrorCode.COUPON_ALREADY_EXISTS);
    }
    return couponRepository.save(coupon);
  }

  public List<Coupon> getAllCoupons() {
    return couponRepository.findAll();
  }
}
