import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserStorageService } from '../../../services/storage/user-storage.service';

@Component({
  selector: 'app-view-wishlist',
  templateUrl: './view-wishlist.component.html',
  styleUrl: './view-wishlist.component.scss',
})
export class ViewWishlistComponent {
  products = [];

  constructor(
    private customerService: CustomerService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.getWishlistByUserId();
  }

  getWishlistByUserId() {
    this.products = [];
    this.customerService.getWishlistByUserId().subscribe((response: any) => {
      response.forEach((element) => {
        element.processedImg = 'data:image/jpeg;base64,' + element.returnedImg;
        this.products.push(element);
      });
    });
  }

  addToCart(id: any) {
    this.customerService.addToCart(id).subscribe(
      (res) => {
        this.snackBar.open('Product added to cart', 'Close', {
          duration: 2000,
        });
      },
      (error) => {
        console.log('error', error.error);
        this.snackBar.open(error.error, 'Close', {
          duration: 2000,
        });
      }
    );
  }

  removeFromWishlist(productId: any) {
    const wishlistDto = {
      productId: productId,
      userId: UserStorageService.getUserId(),
    };

    this.customerService.removeItemFromWishlist(wishlistDto).subscribe(
      (res) => {
        console.log('res remove', res);
        this.snackBar.open('Product removed from wishlist', 'Close', {
          duration: 2000,
        });
        this.getWishlistByUserId();
      },
      (error) => {
        console.log('error', error.error);
        this.snackBar.open(error.error, 'Close', {
          duration: 2000,
        });
      }
    );
  }
}
