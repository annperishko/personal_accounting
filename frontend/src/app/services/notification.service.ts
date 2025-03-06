import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

export interface CustomNotification {
  id: string;
  message: string;
  type: 'success' | 'error' | 'warning' | 'info';
  duration?: number;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private notifications = new BehaviorSubject<CustomNotification[]>([]);

  constructor() {}

  getNotifications(): Observable<CustomNotification[]> {
    return this.notifications.asObservable();
  }

  error(message: string, duration: number = 5000): void {
    this.addNotification({
      id: this.generateId(),
      message,
      type: 'error',
      duration
    });
  }

  success(message: string, duration: number = 5000): void {
    this.addNotification({
      id: this.generateId(),
      message,
      type: 'success',
      duration
    });
  }

  info(message: string, duration: number = 5000): void {
    this.addNotification({
      id: this.generateId(),
      message,
      type: 'info',
      duration
    });
  }

  warning(message: string, duration: number = 5000): void {
    this.addNotification({
      id: this.generateId(),
      message,
      type: 'warning',
      duration
    });
  }

  private addNotification(notification: CustomNotification): void {
    const current = this.notifications.getValue();
    this.notifications.next([...current, notification]);

    if (notification.duration) {
      setTimeout(() => {
        this.removeNotification(notification.id);
      }, notification.duration);
    }
  }

  removeNotification(id: string): void {
    const current = this.notifications.getValue();
    this.notifications.next(current.filter(notification => notification.id !== id));
  }

  private generateId(): string {
    return Math.random().toString(36).substring(2, 11);
  }
}
