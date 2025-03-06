import {Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../entities/user";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";
import {Subscription} from "rxjs";
import {NotificationService} from "../../services/notification.service";

@Component({
  selector: 'oauth-callback',
  templateUrl: './oauth-callback.component.html',
  styleUrls: ['./oauth-callback.component.css']
})
export class OauthCallbackComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private notificationService: NotificationService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      const error = params['error'];

      if (error) {
        this.notificationService.error('Google authentication failed. Please try again.');
        this.router.navigate(['/login']);
        return;
      }

      if (token) {
        localStorage.setItem('JwtToken', token);
        this.notificationService.success('Successfully logged in!');
        this.router.navigate(['/dashboard']);
      }
      else {
        this.notificationService.error('Authentication failed. No token received');
        this.router.navigate(['/login']);
      }
    });
  }
}
