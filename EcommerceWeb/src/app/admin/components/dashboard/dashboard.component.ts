import { Component } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { elementAt } from 'rxjs';
import { log } from 'console';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { title } from 'process';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent {
  products: any[] = [];
  searchProductForm!: FormGroup;

  constructor(
    private adminService: AdminService,
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
    this.adminService.getAllProducts().subscribe(
      (res) => {
        res.forEach((element) => {
          element.processedImg = 'data:image/jpeg;base64,' + element.byteImg;
          this.products.push(element);
          console.log(element);
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
    this.adminService.getAllProductsByName(title).subscribe(
      (res) => {
        res.forEach((element) => {
          element.processedImg = 'data:image/jpeg;base64,' + element.byteImg;
          this.products.push(element);
          console.log(element);
        });
      },
      (error) => {
        console.log(error);
      }
    );
  }

  deleteProduct(productId: any) {
    this.adminService.deleteProduct(productId).subscribe(
      (res) => {
        this.snackBar.open('Product deleted successfully', 'Close', {
          duration: 3000,
        });
        this.getAllProducts();
      },
      (error) => {
        this.snackBar.open(error, 'Close', {
          duration: 3000,
          panelClass: 'error-snackbar'
        });
      }
    );
  }
}
