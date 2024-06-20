package com.nam.ShoppingApp.controller.customer;

import com.nam.ShoppingApp.config.PaymentConfig;
import com.nam.ShoppingApp.dto.PaymentResponseDto;
import com.nam.ShoppingApp.services.customer.cart.CartService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class PaymentController {

  private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
  @Autowired
  private CartService cartService;

  @GetMapping("/payNow/{userId}/{orderId}")
  public ResponseEntity<?> getPay(@PathVariable Long userId, @PathVariable Long orderId)
      throws IOException {

    String vnp_Version = "2.1.0";
    String vnp_Command = "pay";
    String orderType = "other";
    double sum = 0.0;
    sum = (double) cartService.getCartTotalPrice(orderId, userId);
    System.out.println("sum: " + sum);
    long amount = (long) sum * 1000000;
    System.out.println("amount: " + amount);

    String bankCode = "NCB";

    String vnp_TxnRef = PaymentConfig.getRandomNumber(8);
    String vnp_IpAddr = "127.0.0.1";

    String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

    Map<String, String> vnp_Params = new HashMap<>();
    vnp_Params.put("vnp_Version", vnp_Version);
    vnp_Params.put("vnp_Command", vnp_Command);
    vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
    vnp_Params.put("vnp_Amount", String.valueOf(amount));
    vnp_Params.put("vnp_CurrCode", "VND");

    vnp_Params.put("vnp_BankCode", bankCode);
    vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
    vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
    vnp_Params.put("vnp_OrderType", orderType);

    vnp_Params.put("vnp_Locale", "vn");
    vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
    vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

    Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    String vnp_CreateDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

    cld.add(Calendar.MINUTE, 15);
    String vnp_ExpireDate = formatter.format(cld.getTime());
    vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

    List fieldNames = new ArrayList(vnp_Params.keySet());
    Collections.sort(fieldNames);
    StringBuilder hashData = new StringBuilder();
    StringBuilder query = new StringBuilder();
    Iterator itr = fieldNames.iterator();
    while (itr.hasNext()) {
      String fieldName = (String) itr.next();
      String fieldValue = (String) vnp_Params.get(fieldName);
      if ((fieldValue != null) && (fieldValue.length() > 0)) {
        // Build hash data
        hashData.append(fieldName);
        hashData.append('=');
        hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
        // Build query
        query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
        query.append('=');
        query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
        if (itr.hasNext()) {
          query.append('&');
          hashData.append('&');
        }
      }
    }
    String queryUrl = query.toString();
    String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
    queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
    String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;
    log.info("paymentUrl: " + paymentUrl);
//    HttpServletResponse response = null;
//    response.sendRedirect(paymentUrl);
    PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
    paymentResponseDto.setPaymentUrl(paymentUrl);
    return ResponseEntity.ok(paymentResponseDto);
  }
}
