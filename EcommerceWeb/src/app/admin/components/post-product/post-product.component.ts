import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AdminService } from '../../service/admin.service';
import { log } from 'console';

@Component({
  selector: 'app-post-product',
  templateUrl: './post-product.component.html',
  styleUrl: './post-product.component.scss',
})
export class PostProductComponent {
  productForm: FormGroup;
  listOfCategories: any[] = [];
  selectedFile: File | null;
  imagePreview: string | ArrayBuffer | null;

  constructor(
    private formBuilder: FormBuilder,
    private adminService: AdminService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  onFileSelected(event: any) {
    console.log('selected file: ', this.selectedFile);
    this.selectedFile = event.target.files[0];
    this.previewImage();
  }

  previewImage() {
    if (this.selectedFile) {
      console.log('selected file: ', this.selectedFile);
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
    console.log('selected file else: ', this.selectedFile);
  }

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.productForm = this.formBuilder.group({
      name: [null],
      description: [null],
      price: [null],
      categoryId: [null],
    });

    this.getAllCategories();
  }

  getAllCategories() {
    this.adminService.getAllCategories().subscribe(
      (res) => {
        this.listOfCategories = res;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  addProduct(): void {
    if (this.productForm.valid) {
      const formData: FormData = new FormData();
      formData.append('name', this.productForm.get('name')!.value);
      formData.append(
        'description',
        this.productForm.get('description')!.value
      );
      formData.append('price', this.productForm.get('price')!.value);
      formData.append('categoryId', this.productForm.get('categoryId')!.value);
      formData.append('img', this.selectedFile!);

      this.adminService.addProduct(formData).subscribe(
        (res) => {
          this.snackBar.open('Product added successfully', 'Close', {
            duration: 2000,
          });
          this.router.navigateByUrl('/admin/dashboard');
        },
        (error) => {
          this.snackBar.open('Failed to add product', 'Close', {
            duration: 2000,
          });
          console.log(error);
        }
      );
    } else {
      for (const i in this.productForm.controls) {
        this.productForm.controls[i].markAsDirty();
        this.productForm.controls[i].updateValueAndValidity();
      }
    }
  }
}
