package com.nam.ShoppingApp.controller.customer;

import com.nam.ShoppingApp.dto.WishlistDto;
import com.nam.ShoppingApp.services.customer.wishlist.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
public class WishlistController {
    @Autowired private WishlistService wishlistService;

    @PostMapping("/wishlist")
    public ResponseEntity<?> addProductToWishlist(@RequestBody WishlistDto wishlistDto){
        try {
            return ResponseEntity.ok(wishlistService.addProductToWishlist(wishlistDto));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/wishlist/{userId}")
    public ResponseEntity<?> getWishlistByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }
}
