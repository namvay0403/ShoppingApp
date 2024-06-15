import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomerService } from '../../services/customer.service';
import { MatDialog } from '@angular/material/dialog';
import { PlaceOrderComponent } from '../place-order/place-order.component';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss',
})
export class CartComponent {
  cartItems: any[] = [];
  order: any;
  couponForm: any;

  constructor(
    private customerService: CustomerService,
    private snackBar: MatSnackBar,
    private formBuild: FormBuilder,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.couponForm = this.formBuild.group({
      code: [null, [Validators.required]],
    });
    this.getCart();
  }

  applyCoupon() {
    this.customerService
      .applyCoupon(this.couponForm.get(['code']).value)
      .subscribe(
        (res) => {
          this.snackBar.open('Coupon Applied Successfully', 'Close', {
            duration: 2000,
          });
          this.getCart();
        },
        (error) => {
          this.snackBar.open(error.error, 'Close', {
            duration: 2000,
          });
        }
      );
  }

  getCart() {
    this.cartItems = [];
    this.customerService.getCartByUserId().subscribe(
      (res) => {
        this.order = res;
        res.cartItems.forEach((element) => {
          element.processedImg =
            'data:image/jpeg;base64,' + element.returnedImg;
          this.cartItems.push(element);
          console.log(element);
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  increaseQuantity(productId: any) {
    this.customerService
      .increaseProductQuantity(productId)
      .subscribe((res) => this.getCart());
  }

  decreaseQuantity(productId: any) {
    this.customerService
      .decreaseProductQuantity(productId)
      .subscribe((res) => this.getCart());
  }

  placeOrder() {
    this.dialog.open(PlaceOrderComponent)
  }
}
