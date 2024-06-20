import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomerService } from '../../services/customer.service';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-place-order',
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.scss',
})
export class PlaceOrderComponent {
  orderForm: FormGroup;

  constructor(
    private customerService: CustomerService,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.orderForm = this.formBuilder.group({
      address: [null, [Validators.required]],
      orderDescription: [null, [Validators.required]],
    });
  }

  placeOrder() {
    this.customerService.placeOrder(this.orderForm.value).subscribe(
      (res) => {
        this.snackBar.open('Order Placed Successfully', 'Close', {
          duration: 2000,
        });
        this.router.navigateByUrl('/customer/my_orders');
        this.closeForm();
        // this.payNow(res.orderId);
      },
      (error) => {
        this.snackBar.open(error.error, 'Close', {
          duration: 2000,
        });
      }
    );
  }

  payNow(orderId: any) {
    this.customerService.payNow(orderId).subscribe((res) => {
      // this.snackBar.open('Payment Successful', 'Close', {
      //   duration: 2000,
      // });
      console.log(res.paymentUrl);
      window.location.href = res.paymentUrl;
    });
  }

  closeForm() {
    this.dialog.closeAll();
  }
}
