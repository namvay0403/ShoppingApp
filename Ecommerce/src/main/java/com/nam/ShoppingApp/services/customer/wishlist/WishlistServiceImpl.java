package com.nam.ShoppingApp.services.customer.wishlist;

import com.nam.ShoppingApp.dto.WishlistDto;
import com.nam.ShoppingApp.entity.Product;
import com.nam.ShoppingApp.entity.User;
import com.nam.ShoppingApp.entity.Wishlist;
import com.nam.ShoppingApp.repository.ProductRepository;
import com.nam.ShoppingApp.repository.UserRepository;
import com.nam.ShoppingApp.repository.WishlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WishlistServiceImpl implements  WishlistService{
    @Autowired private WishlistRepository wishlistRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    public WishlistDto addProductToWishlist(WishlistDto wishlistDto){
        Optional<Product> product = productRepository.findById(wishlistDto.getProductId());
        Optional<User> user = userRepository.findById(wishlistDto.getUserId());

        Optional<Wishlist> wishlistOptional = wishlistRepository.findByProductIdAndUserId(wishlistDto.getProductId(), wishlistDto.getUserId());

        if (wishlistOptional.isPresent())
        {
            throw new RuntimeException("Product already in wishlist");
        }

        if(product.isPresent() && user.isPresent()){
            Wishlist wishlist = new Wishlist();
            wishlist.setProduct(product.get());
            wishlist.setUser(user.get());

            return wishlistRepository.save(wishlist).getWishlistDto();
        }
        return null;
    }

    public List<WishlistDto> getWishlistByUserId(Long userId){
        List<Wishlist> wishlistList = wishlistRepository.findAllByUserId(userId);
        return wishlistList.stream().map(Wishlist::getWishlistDto).toList();
    }

    public WishlistDto removeItemFromWishlist(WishlistDto wishlistDto){
        Optional<Wishlist> wishlist = wishlistRepository.findByProductIdAndUserId(wishlistDto.getProductId(), wishlistDto.getUserId());
        log.info("wishlist: " + wishlist);
        log.info("wishlistDto: " + wishlistDto);
        if(wishlist.isPresent()){
            wishlistRepository.delete(wishlist.get());
            return wishlistDto;
        }
        return null;
    }
}
