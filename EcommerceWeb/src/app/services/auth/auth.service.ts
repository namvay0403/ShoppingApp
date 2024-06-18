import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { UserStorageService } from '../storage/user-storage.service';
import { AnySrvRecord } from 'dns';

const BASIC_URL = 'http://localhost:8080/';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private http: HttpClient,
    private userStorageService: UserStorageService
  ) {}

  register(signupRequest: any): Observable<any> {
    return this.http.post(BASIC_URL + 'signup', signupRequest);
  }

  login(username: string, password: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = { username, password };

    return this.http
      .post(BASIC_URL + 'authenticate', body, { headers, observe: 'response' })
      .pipe(
        map((response) => {
          const token = response.headers.get('authorization').substring(7);
          const user = response.body;

          if (token && user) {
            this.userStorageService.saveUser(user);
            this.userStorageService.saveToken(token);
            return true;
          }
          return false;
        })
      );
  }

  getOrderTrackingId(trackingId: number) {
    return this.http.get(BASIC_URL + `order/${trackingId}`);
  }
}
