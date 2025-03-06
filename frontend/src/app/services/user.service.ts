import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {EMPTY, Observable, throwError} from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { NotificationService } from './notification.service';
import { JwtDecoderService } from './jwt-decoder.service';
import {User} from "../entities/user";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8082/user';

  constructor(
    private http: HttpClient,
    private router: Router,
    private notificationService: NotificationService,
    private jwtService: JwtDecoderService
  ) { }

  getUserByEmail(email: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${email}`)
      .pipe(
        tap(user => {
          return user;
        }),
        catchError(error => {
          if (error.status === 401) {
            this.notificationService.error('Your session has expired. Please log in again.');
            this.router.navigate(['/login']);
          } else {
            this.notificationService.error('Failed to load user data.');
          }
          return throwError(() => error);
        })
      );
  }

  getUserProfile(): Observable<User> {
    if (!this.jwtService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return EMPTY;
    }

    const email = this.jwtService.getUserEmail();

    if (!email) {
      this.notificationService.error('Unable to identify user. Please log in again.');
      this.router.navigate(['/login']);
      return throwError(() => new Error('User not authenticated'));
    }

    return this.getUserByEmail(email);
  }

  updateUser(user: User): Observable<any> {
    return this.http.put(`${this.baseUrl}`, user)
      .pipe(
        tap(() => {
          this.notificationService.success('User profile updated successfully');
        }),
        catchError(error => {
          this.notificationService.error('Failed to update user profile');
          return throwError(() => error);
        })
      );
  }
}
