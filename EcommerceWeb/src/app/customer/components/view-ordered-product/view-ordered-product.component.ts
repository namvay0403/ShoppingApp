import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-view-ordered-product',
  templateUrl: './view-ordered-product.component.html',
  styleUrl: './view-ordered-product.component.scss',
})
export class ViewOrderedProductComponent {
  orderId: number = this.activatedRoute.snapshot.params['orderId'];
  orderedProductDetailsList = [];
  totalAmount: any;

  constructor(
    private activatedRoute: ActivatedRoute,
    private customerService: CustomerService
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.getOrderedProducts();
  }

  getOrderedProducts() {
    // API call to get ordered products
    this.customerService
      .getOrderedProducts(this.orderId)
      .subscribe((response) => {
        response.productDtoList.forEach((element) => {
          element.processedImg = 'data:image/jpeg;base64,' + element.byteImg;
          this.orderedProductDetailsList.push(element);
        });
        this.totalAmount = response.orderAmount;
      });
  }
}
