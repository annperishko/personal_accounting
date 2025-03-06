import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {EMPTY, Observable} from 'rxjs';
import { Transaction } from '../entities/transaction';
import {NotificationService} from "./notification.service";
import {JwtDecoderService} from "./jwt-decoder.service";

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private baseUrl = 'http://localhost:8082/account';

  constructor(private http: HttpClient,
              private notificationService: NotificationService,
              private jwtDecoderService: JwtDecoderService) {

  }


  saveTransaction(transaction: Transaction): Observable<any> {
    let email = this.jwtDecoderService.getUserEmail();

    if(email) {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('JwtToken')}`
      });

      return this.http.post(`${this.baseUrl}/${email}`, transaction, {headers});
    }
    else {
      this.notificationService.error('Failed to save transaction');
      return EMPTY;
    }
  }

}
