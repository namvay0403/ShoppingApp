import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../../admin/service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  products: any[] = [];
  searchProductForm!: FormGroup;

  constructor(
    private customerService: CustomerService,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.getAllProducts();
    this.searchProductForm = this.formBuilder.group({
      title: [null, [Validators.required]],
    });
  }

  getAllProducts() {
    this.products = [];
    this.customerService.getAllProducts().subscribe(
      (res) => {
        res.forEach((element) => {
          element.processedImg = 'data:image/jpeg;base64,' + element.byteImg;
          this.products.push(element);
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  submitForm() {
    this.products = [];
    const title = this.searchProductForm.get('title')?.value;
    if (title === null || title === '') {
      this.getAllProducts();
      return;
    }
    this.customerService.getAllProductsByName(title).subscribe((res) => {
      res.forEach((element) => {
        element.processedImg = 'data:image/jpeg;base64,' + element.byteImg;
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
}
