import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { error, log } from 'node:console';

@Component({
  selector: 'app-post-coupon',
  templateUrl: './post-coupon.component.html',
  styleUrl: './post-coupon.component.scss',
})
export class PostCouponComponent {
  couponForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private adminService: AdminService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.couponForm = this.formBuilder.group({
      code: [null, [Validators.required]],
      discount: [null, [Validators.required]],
      name: [null, [Validators.required]],
      expirationDate: [null, [Validators.required]],
    });
  }

  addCoupon() {
    if (this.couponForm.valid) {
      this.adminService.addCoupon(this.couponForm.value).subscribe(
        (response) => {
          this.snackBar.open('Coupon added successfully', 'Close', {
            duration: 2000,
          });
          this.router.navigateByUrl('/admin/dashboard');
        },
        (error) => {
          this.snackBar.open(error.error['message'], 'Close', {
            duration: 2000,
          });
          console.log(error);
        }
      );
    } else {
      this.couponForm.markAllAsTouched();
    }
  }
}
