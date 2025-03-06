import {Component, OnDestroy} from '@angular/core';
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {User} from "../../entities/user";
import {Subscription} from "rxjs";
import {NotificationService} from "../../services/notification.service";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy
{

  public user: User;
  private subscription: Subscription | undefined;
  public isAccountLocked: boolean = false;
  public errorMessage: string = '';

  constructor(private router: Router,
              private authService: AuthService,
              private notificationService: NotificationService)
  {
    this.user = new User();
  }

  ngOnDestroy(): void
  {
    if(this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.isAccountLocked = false;

    this.subscription = this.authService.login(this.user).subscribe({
      next: () => {
        this.notificationService.success('Successfully logged in!');
        this.router.navigate(['/dashboard']);
      },
      error: (error: HttpErrorResponse) => {
        if (error.status === 401) {
          const errorObj = error.error;
          if (errorObj && errorObj.errorType === 'ACCOUNT_LOCKED') {
            this.isAccountLocked = true;
            console.error( `Account is locked due to too many failed attempts. Please try again in ${errorObj.lockTimeRemaining || 'a few minutes'}.`);
          } else {
            console.error( 'Invalid email or password');
          }
        } else {
          console.error( 'Login failed. Please try again later.');
        }

        this.notificationService.error('Failed to logged in!');
        console.error('Login failed', error);
      }
    });
  }


  loginWithGoogle(): void {
    this.authService.loginWithGoogle();
  }

  public routeToRegistration()
  {
    this.router.navigate(['/register']);
  }

}
