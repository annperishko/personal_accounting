import {Component, OnDestroy, OnInit} from "@angular/core";
import { Router } from "@angular/router";
import { TransactionService } from "../../services/transaction.service";
import { NotificationService } from "../../services/notification.service";
import { Transaction } from "../../entities/transaction";
import { formatDate } from "@angular/common";
import { HttpErrorResponse } from '@angular/common/http';
import {UserService} from "../../services/user.service";
import {Subscription} from "rxjs";
import {User} from "../../entities/user";

@Component({
  selector: 'dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit, OnDestroy {
  userName: string = 'User';
  showProfileDropdown: boolean = false;

  currentUser: User;

  transaction: Transaction = new Transaction();
  transactionTypes: string[] = ['EARNING', 'EXPENSE'];
  availableCategories: string[] = [];
  currentDate: string = formatDate(new Date(), 'yyyy-MM-dd', 'en');

  earningCategories: string[] = ['SALARY', 'POCKET', 'OTHER_EARNING'];
  expenseCategories: string[] = ['FOOD', 'MEDICINE', 'EDUCATION', 'OTHER_EXPENSE'];

  private subscription: Subscription = new Subscription();

  constructor(
    private router: Router,
    private transactionService: TransactionService,
    private notificationService: NotificationService,
    private userService: UserService
  ) {
    this.currentUser = new User();
  }

  ngOnInit(): void {
    this.loadUserProfile();

    console.warn(this.currentUser)
    this.initializeTransaction();
  }

  ngOnDestroy(): void
  {
    if(this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  initializeTransaction(): void {
    this.transaction = new Transaction();
    this.transaction.transactionDate = this.currentDate;
  }

  loadUserProfile(): void {
    this.subscription = this.userService.getUserProfile().subscribe({
      next: (userData) => {
        this.currentUser = userData;
        if (userData.name && userData.surname) {
          this.userName = `${userData.name} ${userData.surname}`;
        }
      },
      error: (error) => {
        console.error('Error loading user profile:', error);
      }
    });

  }

  onTransactionTypeChange(): void {
    if (this.transaction.transactionType === 'EARNING') {
      this.availableCategories = this.earningCategories;
    } else if (this.transaction.transactionType === 'EXPENSE') {
      this.availableCategories = this.expenseCategories;
    } else {
      this.availableCategories = [];
    }
    this.transaction.category = null;
  }

  isFormValid(): boolean {
    return !!(
      this.transaction.transactionType &&
      this.transaction.category &&
      this.transaction.amount &&
      this.transaction.transactionDate
    );
  }

  saveTransaction(): void {
    if (!this.isFormValid()) {
      this.notificationService.error('Please fill all required fields');
      return;
    }

    const formattedTransaction = {
      ...this.transaction,
      amount: Number(this.transaction.amount)
    };

    this.transactionService.saveTransaction(formattedTransaction).subscribe({
      next: () => {
        this.notificationService.success('Transaction saved successfully');
        this.initializeTransaction();
      },
      error: (error: HttpErrorResponse) => {

        if (error.status === 409) {
          this.notificationService.error('Transaction could not be saved: Category is not compatible with transaction type');
        } else if (error.status === 400) {
          this.notificationService.error('Transaction could not be saved: Invalid data');
        } else if (error.status === 401) {
          this.notificationService.error('Your session has expired. Please log in again.');
          this.logOut();
        } else {
          this.notificationService.error('Failed to save transaction. Please try again.');
        }
      }
    });
  }

  logOut(): void {
    localStorage.removeItem('JwtToken');
    this.router.navigate(['/login']);
  }
}
