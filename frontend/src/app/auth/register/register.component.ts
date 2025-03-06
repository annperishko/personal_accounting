import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../entities/user";
import {Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {Subscription} from "rxjs";
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy
{
  public user: User;
  public errorMessage: string = '';

  private subscription: Subscription | undefined;

  constructor(private router: Router,
              private authService: AuthService,
              private notificationService: NotificationService) {

    this.user = new User();

  }

  ngOnInit()
  {
  }

  ngOnDestroy(): void
  {
    if(this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  public onSubmit(): void {
    this.errorMessage = '';

    this.subscription = this.authService.register(this.user).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      }
    });
  }

  public goBack(): void {
    this.router.navigate(['/login']);
  }

  isFormValid(): boolean {
    return !!(this.user.name &&
        this.user.surname &&
        this.user.email &&
        this.user.password);
  }
}
