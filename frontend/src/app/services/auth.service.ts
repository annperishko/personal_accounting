import {User} from "../entities/user";
import {catchError, EMPTY, firstValueFrom, Observable, take, tap, throwError} from "rxjs";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {NotificationService} from "./notification.service";

@Injectable()
export class AuthService
{

  private baseUrl = 'http://localhost:8082/api/auth';

  constructor(private http: HttpClient,
              private notificationService: NotificationService) {}

  login(loginRequest: User): Observable<JwtAuthenticationResponse> {
    return this.http.post<JwtAuthenticationResponse>(`${this.baseUrl}/login`, loginRequest).pipe(
      tap(response => {
        if (response.accessToken) {
          localStorage.setItem('JwtToken', response.accessToken);
          localStorage.setItem('UserEmail', response.email);
        }
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          const errorResponse = error.error;

          if (errorResponse && errorResponse.errorType === 'ACCOUNT_LOCKED') {
            const lockTimeRemaining = errorResponse.lockTimeRemaining || 'some time';
            this.notificationService.error(`Account is locked due to too many failed attempts. Please try again in ${lockTimeRemaining}.`);
          } else {
            this.notificationService.error('Invalid email or password');
          }
        } else {
          this.notificationService.error('Login failed. Please try again later.');
        }

        return throwError(() => error);
      })
    );
  }

  register(registerRequest: User): Observable<any> {
    const url = `${this.baseUrl}/register`;

    return this.http.post(url, registerRequest, {
      headers: {
        'Content-Type': 'application/json'
      },
      observe: 'response'
    }).pipe(
      tap(() => {
        this.notificationService.success('Registration successful!');
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 409) {
          this.notificationService.error('Email already in use');

        }
        this.notificationService.error('Registration failed. Please try again');
        return EMPTY;
      })
    );
  }

  loginWithGoogle(): void {
    this.http.get<{authUrl: string}>(`${this.baseUrl}/google/auth-url`)
      .pipe(
        take(1),
        catchError(error => {
          this.notificationService.error('Failed to connect to Google login. Please try again later.');
          return EMPTY;
        })
      )
      .subscribe(response => {
        window.location.href = response.authUrl;
      });
  }
}

interface JwtAuthenticationResponse {
  accessToken: string;
  email: string;
}
