import { Component } from '@angular/core';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-my-orders',
  templateUrl: './my-orders.component.html',
  styleUrl: './my-orders.component.scss',
})
export class MyOrdersComponent {
  myOrders: any;

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.getMyOrders();
  }

  getMyOrders() {
    this.customerService.getOrderByUserId().subscribe((res) => {
      this.myOrders = res;
    });
  }
}
