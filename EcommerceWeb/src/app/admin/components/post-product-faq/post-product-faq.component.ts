import { Component } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-post-product-faq',
  templateUrl: './post-product-faq.component.html',
  styleUrl: './post-product-faq.component.scss',
})
export class PostProductFaqComponent {
  productId: number = this.activatedRoute.snapshot.params['productId'];
  FAQForm: FormGroup;

  constructor(
    private adminService: AdminService,
    private router: Router,
    private snackBar: MatSnackBar,
    private activatedRoute: ActivatedRoute,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.FAQForm = this.formBuilder.group({
      question: [null, [Validators.required]],
      answer: [null, [Validators.required]],
    });
  }

  postFAQ(): void {
    if (this.FAQForm.invalid) {
      this.FAQForm.markAllAsTouched();
      return;
    }

    console.log('data to be posted: ', this.FAQForm.value, this.productId);

    this.adminService.postFAQ(this.productId, this.FAQForm.value).subscribe(
      (response) => {
        this.snackBar.open('FAQ added successfully', 'Close', {
          duration: 2000,
        });
        this.router.navigateByUrl('/admin/dashboard');
      },
      (error) => {
        this.snackBar.open(error.error, 'Close', {
          duration: 2000,
        });
      }
    );
  }
}
