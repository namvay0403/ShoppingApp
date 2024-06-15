import { Component } from '@angular/core';
import { AdminService } from '../../service/admin.service';

@Component({
  selector: 'app-coupons',
  templateUrl: './coupons.component.html',
  styleUrl: './coupons.component.scss'
})
export class CouponsComponent {
  coupons: any;
  
  constructor(
    private adminService: AdminService,
  ) {}

  ngOnInit(): void {
    //Called after the constructor, initializing input properties, and the first call to ngOnChanges.
    //Add 'implements OnInit' to the class.
    this.getCoupons();
  }

  getCoupons() {
    this.adminService.getCoupons().subscribe((response) => {
      this.coupons = response;
    });
  }
}
