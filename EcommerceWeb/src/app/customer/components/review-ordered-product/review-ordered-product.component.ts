import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserStorageService } from '../../../services/storage/user-storage.service';

@Component({
  selector: 'app-review-ordered-product',
  templateUrl: './review-ordered-product.component.html',
  styleUrl: './review-ordered-product.component.scss',
})
export class ReviewOrderedProductComponent {
  productId: number = this.route.snapshot.params['productId'];
  reviewForm: FormGroup;
  selectedFile: File | null;
  imagePreview: string | ArrayBuffer | null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private customerService: CustomerService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.reviewForm = this.fb.group({
      rating: [null, Validators.required],
      description: [null, Validators.required],
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    this.previewImage();
  }

  previewImage(): void {
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    };
    reader.readAsDataURL(this.selectedFile);
  }

  submitForm(): void {
    const formData = new FormData();
    formData.append('rating', this.reviewForm.value.rating);
    formData.append('description', this.reviewForm.value.description);
    formData.append('img', this.selectedFile);
    formData.append('productId', this.productId.toString());
    formData.append('userId', UserStorageService.getUserId());

    this.customerService.giveReview(formData).subscribe(
      (response) => {
        this.snackBar.open('Review submitted successfully', 'Close', {
          duration: 2000,
        });
        this.router.navigate(['/customer/my_orders']);
      },
      (error) => {
        this.snackBar.open('Error submitting review', 'Close', {
          duration: 2000,
        });
        console.log(error.error);
      }
    );
  }
}
