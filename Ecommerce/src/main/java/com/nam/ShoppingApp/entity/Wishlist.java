package com.nam.ShoppingApp.entity;

import com.nam.ShoppingApp.dto.WishlistDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public WishlistDto getWishlistDto() {
        WishlistDto wishlistDto = new WishlistDto();
        wishlistDto.setId(this.id);
        wishlistDto.setProductId(this.product.getId());
        wishlistDto.setUserId(this.user.getId());
        wishlistDto.setReturnedImg(this.product.getImg());
        wishlistDto.setPrice(this.product.getPrice());
        wishlistDto.setProductName(this.product.getName());
        wishlistDto.setProductDescription(this.product.getDescription());

        return wishlistDto;
    }
}
