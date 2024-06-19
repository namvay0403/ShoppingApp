package com.nam.ShoppingApp.services.customer.wishlist;

import com.nam.ShoppingApp.dto.WishlistDto;

import java.util.List;

public interface WishlistService {
    WishlistDto addProductToWishlist(WishlistDto wishlistDto);
    List<WishlistDto> getWishlistByUserId(Long userId);
    WishlistDto removeItemFromWishlist(WishlistDto wishlistDto);
}
