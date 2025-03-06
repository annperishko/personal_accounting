import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {RouterModule, Routes} from "@angular/router";
import {LoginComponent} from "./auth/login/login.component";
import {FormsModule} from "@angular/forms";
import {RegisterComponent} from "./auth/register/register.component";
import {AuthService} from "./services/auth.service";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {DashboardComponent} from "./accounting/dashboard/dashboard.component";
import {OauthCallbackComponent} from "./auth/oAuth/oauth-callback.component";
import { NotificationComponent } from './notification/notification.component';
import {TransactionService} from "./services/transaction.service";
import {AuthInterceptor} from "./services/auth-interceptor";
import {JwtDecoderService} from "./services/jwt-decoder.service";
import {UserService} from "./services/user.service";

const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'dashboard', component: DashboardComponent },
    { path: 'oauth2/callback', component: OauthCallbackComponent },
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        RegisterComponent,
        DashboardComponent,
        OauthCallbackComponent,
        NotificationComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        BrowserAnimationsModule,
        RouterModule.forRoot(routes),
        HttpClientModule,

    ],
    providers: [
      AuthService,
      TransactionService,
      JwtDecoderService,
      UserService,
      {
        provide: HTTP_INTERCEPTORS,
        useClass: AuthInterceptor,
        multi: true
      }
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
