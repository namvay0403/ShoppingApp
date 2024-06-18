import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserStorageService } from '../../../services/storage/user-storage.service';
import { error, log } from 'console';

@Component({
  selector: 'app-view-product-detail',
  templateUrl: './view-product-detail.component.html',
  styleUrl: './view-product-detail.component.scss',
})
export class ViewProductDetailComponent {
  productId: number = this.activatedRoute.snapshot.params['productId'];

  product: any;
  FAQs = [];
  reviews = [];

  constructor(
    private customerService: CustomerService,
    private activatedRoute: ActivatedRoute,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.getProductDetailById();
  }

  getProductDetailById() {
    this.customerService
      .getProductDetailById(this.productId)
      .subscribe((response: any) => {
        this.product = response.productDto;
        this.FAQs = response.faqDtoList;
        this.product.processedImg =
          'data:image/jpeg;base64,' + response.productDto.byteImg;

        response.reviewDtoList.forEach((element) => {
          element.processedImg =
            'data:image/jpeg;base64,' + element.returnedImg;
          this.reviews.push(element);
        });
      });
  }

  addProductToWishlist() {
    const wishlistDto = {
      productId: this.productId,
      userId: UserStorageService.getUserId(),
    };

    this.customerService.addProductToWishlist(wishlistDto).subscribe(
      (response: any) => {
        if (response.id != null) {
          this.snackBar.open(
            'Product added to wishlist successfully',
            'Close',
            {
              duration: 2000,
            }
          );
          console.log('res catch' + response);
        }
      },
      (error) => {
        this.snackBar.open(error.error, 'Close', {
          duration: 2000,
        });
        console.log('error catch' + error);
      }
    );
  }
}
