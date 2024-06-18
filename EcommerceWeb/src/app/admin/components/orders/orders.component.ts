import { Component } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AdminService } from '../../service/admin.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrl: './orders.component.scss',
})
export class OrdersComponent {
  orders: any;

  constructor(
    private snackBar: MatSnackBar,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.getPlacedOrders();
  }

  getPlacedOrders() {
    this.adminService.getPlacedOrders().subscribe((response) => {
      this.orders = response;
    });
  }

  changeOrderStatus(orderId: number, status: string) {
    console.log(orderId, status);
    this.adminService
      .changeOrderStatus(orderId, status)
      .subscribe((response) => {
        if (response.id != null) {
          this.snackBar.open('Order status updated successfully', 'Close', {
            duration: 2000,
          });
          this.getPlacedOrders();
        } else {
          console.log(response.message);
          this.snackBar.open(response.message, 'Close', {
            duration: 2000,
          });
        }
      });
  }
}
