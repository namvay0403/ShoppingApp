import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth/auth.service';

@Component({
  selector: 'app-track-order',
  templateUrl: './track-order.component.html',
  styleUrl: './track-order.component.scss',
})
export class TrackOrderComponent {
  searchOrderForm: FormGroup;
  order: any;

  constructor(private fb: FormBuilder, private authService: AuthService) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.searchOrderForm = this.fb.group({
      trackingId: [null, Validators.required],
    });
  }

  submitForm(): void {
    this.authService
      .getOrderTrackingId(this.searchOrderForm.get('trackingId').value)
      .subscribe((response) => {
        console.log(response);
        this.order = response;
      });
  }
}
