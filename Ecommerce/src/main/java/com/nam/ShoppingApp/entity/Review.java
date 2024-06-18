package com.nam.ShoppingApp.entity;

import com.nam.ShoppingApp.dto.ReviewDto;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rating;

    @Lob
    private String description;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] img;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    public ReviewDto getReviewDto(){
        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setId(this.id);
        reviewDto.setRating(this.rating);
        reviewDto.setDescription(this.description);
        reviewDto.setReturnedImg(this.img);
        reviewDto.setUserId(this.user.getId());
        reviewDto.setProductId(this.product.getId());
        reviewDto.setUsername(this.user.getName());
        return reviewDto;
    }
}
