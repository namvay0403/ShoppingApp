import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AdminService } from '../../service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-update-product',
  templateUrl: './update-product.component.html',
  styleUrl: './update-product.component.scss',
})
export class UpdateProductComponent {
  productForm: FormGroup;
  listOfCategories: any[] = [];
  selectedFile: File | null;
  imagePreview: string | ArrayBuffer | null;
  productId: number = this.activatedRoute.snapshot.params['productId'];
  existingImage: string | ArrayBuffer | null;
  imgChanged: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private adminService: AdminService,
    private snackBar: MatSnackBar,
    private router: Router,
    private activatedRoute: ActivatedRoute
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
        this.imgChanged = true;

        this.existingImage = null;
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
    this.getProductById(this.productId);
  }

  getProductById(productId: number) {
    this.adminService.getProductById(productId).subscribe(
      (res) => {
        this.productForm.patchValue({
          name: res.name,
          description: res.description,
          price: res.price,
          categoryId: res.categoryId,
        });
        this.existingImage = 'data:image/jpeg;base64,' + res.byteImg;
      },
      (error) => {
        console.log(error);
      }
    );
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

  updateProduct(): void {
    if (this.productForm.valid) {
      console.log(
        'data to be updated: ',
        this.productForm.value,
        this.productId
      );

      const formData: FormData = new FormData();

      if (this.imgChanged && this.selectedFile) {
        formData.append('img', this.selectedFile!);
      }

      formData.append('name', this.productForm.get('name')!.value);
      formData.append(
        'description',
        this.productForm.get('description')!.value
      );
      formData.append('price', this.productForm.get('price')!.value);
      formData.append('categoryId', this.productForm.get('categoryId')!.value);

      this.adminService.updateProduct(this.productId, formData).subscribe(
        (res) => {
          this.snackBar.open('Product updated successfully', 'Close', {
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
