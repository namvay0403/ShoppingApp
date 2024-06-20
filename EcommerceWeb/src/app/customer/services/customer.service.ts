import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserStorageService } from '../../services/storage/user-storage.service';

const BASE_URL = 'http://localhost:8080/';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  constructor(private http: HttpClient) {}

  getAllProducts(): Observable<any> {
    return this.http.get(BASE_URL + 'api/customer/products', {
      headers: this.createAuthorizationHeader(),
    });
  }

  getAllProductsByName(name: any): Observable<any> {
    return this.http.get(BASE_URL + `api/customer/search/${name}`, {
      headers: this.createAuthorizationHeader(),
    });
  }

  addToCart(productId: any): Observable<any> {
    const cartDto = {
      productId: productId,
      userId: UserStorageService.getUserId(),
    };
    return this.http.post(BASE_URL + `api/customer/cart`, cartDto, {
      headers: this.createAuthorizationHeader(),
    });
  }

  removeItemFromCart(productId: any): Observable<any> {
    const cartDto = {
      productId: productId,
      userId: UserStorageService.getUserId(),
    };
    return this.http.post(BASE_URL + `api/customer/cart/removal`, cartDto, {
      headers: this.createAuthorizationHeader(),
    });
  }

  getCartByUserId(): Observable<any> {
    const userId = UserStorageService.getUserId();
    return this.http.get(BASE_URL + `api/customer/cart/${userId}`, {
      headers: this.createAuthorizationHeader(),
    });
  }

  applyCoupon(code: any): Observable<any> {
    const userId = UserStorageService.getUserId();
    return this.http.get(BASE_URL + `api/customer/coupon/${userId}/${code}`, {
      headers: this.createAuthorizationHeader(),
    });
  }

  increaseProductQuantity(productId: any): Observable<any> {
    const cartDto = {
      productId: productId,
      userId: UserStorageService.getUserId(),
    };
    return this.http.post(BASE_URL + `api/customer/addition`, cartDto, {
      headers: this.createAuthorizationHeader(),
    });
  }

  decreaseProductQuantity(productId: any): Observable<any> {
    const cartDto = {
      productId: productId,
      userId: UserStorageService.getUserId(),
    };
    return this.http.post(BASE_URL + `api/customer/subtraction`, cartDto, {
      headers: this.createAuthorizationHeader(),
    });
  }

  placeOrder(orderDto: any): Observable<any> {
    orderDto.userId = UserStorageService.getUserId();
    return this.http.post(BASE_URL + `api/customer/placeOrder`, orderDto, {
      headers: this.createAuthorizationHeader(),
    });
  }

  getOrderByUserId(): Observable<any> {
    const userId = UserStorageService.getUserId();
    return this.http.get(BASE_URL + `api/customer/myOrders/${userId}`, {
      headers: this.createAuthorizationHeader(),
    });
  }

  getOrderedProducts(orderId: any): Observable<any> {
    const userId = UserStorageService.getUserId();
    return this.http.get(
      BASE_URL + `api/customer/ordered-products/${orderId}`,
      {
        headers: this.createAuthorizationHeader(),
      }
    );
  }

  getProductDetailById(productId: any): Observable<any> {
    return this.http.get(BASE_URL + `api/customer/product/${productId}`, {
      headers: this.createAuthorizationHeader(),
    });
  }

  giveReview(reviewDto: any): Observable<any> {
    return this.http.post(BASE_URL + `api/customer/review`, reviewDto, {
      headers: this.createAuthorizationHeader(),
    });
  }

  addProductToWishlist(wishlistDto: any): Observable<any> {
    return this.http.post(BASE_URL + `api/customer/wishlist`, wishlistDto, {
      headers: this.createAuthorizationHeader(),
    });
  }

  removeItemFromWishlist(wishlistDto: any): Observable<any> {
    return this.http.post(
      BASE_URL + `api/customer/wishlist/removal`,
      wishlistDto,
      {
        headers: this.createAuthorizationHeader(),
      }
    );
  }

  getWishlistByUserId(): Observable<any> {
    const userId = UserStorageService.getUserId();
    return this.http.get(BASE_URL + `api/customer/wishlist/${userId}`, {
      headers: this.createAuthorizationHeader(),
    });
  }

  payNow(orderId: any): Observable<any> {
    const userId = UserStorageService.getUserId();
    return this.http.get(
      BASE_URL + `api/customer/payNow/${userId}/${orderId}`,
      {
        headers: this.createAuthorizationHeader(),
      }
    );
  }

  private createAuthorizationHeader() {
    return new HttpHeaders().set(
      'Authorization',
      'Bearer ' + UserStorageService.getToken()
    );
  }
}
