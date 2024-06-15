import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserStorageService } from '../../services/storage/user-storage.service';

const BASE_URL = 'http://localhost:8080/';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  constructor(private http: HttpClient) {}

  addCategory(categoryDto: any): Observable<any> {
    return this.http.post(BASE_URL + 'api/admin/category', categoryDto, {
      headers: this.createAuthorizationHeader(),
    });
  }

  addProduct(productDto: any): Observable<any> {
    return this.http.post(BASE_URL + 'api/admin/product', productDto, {
      headers: this.createAuthorizationHeader(),
    });
  }

  addCoupon(couponDto: any): Observable<any> {
    return this.http.post(BASE_URL + 'api/admin/coupons', couponDto, {
      headers: this.createAuthorizationHeader(),
    });
  }

  getCoupons(): Observable<any> {
    return this.http.get(BASE_URL + 'api/admin/coupons', {
      headers: this.createAuthorizationHeader(),
    });
  }

  getPlacedOrders(): Observable<any> {
    return this.http.get(BASE_URL + 'api/admin/placedOrders', {
      headers: this.createAuthorizationHeader(),
    });
  }

  getAllCategories(): Observable<any> {
    return this.http.get(BASE_URL + 'api/admin/categories', {
      headers: this.createAuthorizationHeader(),
    });
  }

  getAllProducts(): Observable<any> {
    return this.http.get(BASE_URL + 'api/admin/products', {
      headers: this.createAuthorizationHeader(),
    });
  }

  getAllProductsByName(name: any): Observable<any> {
    return this.http.get(BASE_URL + `api/admin/search/${name}`, {
      headers: this.createAuthorizationHeader(),
    });
  }

  deleteProduct(productId: any): Observable<any> {
    return this.http.delete(BASE_URL + `api/admin/product/${productId}`, {
      headers: this.createAuthorizationHeader(),
    });
  }

  private createAuthorizationHeader() {
    return new HttpHeaders().set(
      'Authorization',
      'Bearer ' + UserStorageService.getToken()
    );
  }
}
