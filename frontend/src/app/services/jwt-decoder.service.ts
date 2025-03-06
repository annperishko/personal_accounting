import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class JwtDecoderService {

  constructor() { }

  /**
   * Decode JWT token to extract payload
   */
  decodeToken(token: string): any {
    if (!token) {
      return null;
    }

    const parts = token.split('.');
    if (parts.length !== 3) {
      return null;
    }

    try {
      const payload = JSON.parse(atob(parts[1]));
      return payload;
    } catch (e) {
      console.error('Error decoding JWT token:', e);
      return null;
    }
  }

  /**
   * Get user email from JWT token
   */
  getUserEmail(): string | null {
    const token = localStorage.getItem('JwtToken');
    if (!token) {
      return null;
    }

    const payload = this.decodeToken(token);
    if (!payload) {
      return null;
    }

    return payload.sub;
  }

  /**
   * Check if the token is expired
   */
  isTokenExpired(): boolean {
    const token = localStorage.getItem('JwtToken');
    if (!token) {
      return true;
    }

    const payload = this.decodeToken(token);
    if (!payload || !payload.exp) {
      return true;
    }

    const expirationDate = new Date(payload.exp * 1000);
    return expirationDate < new Date();
  }

  /**
   * Check if user is authenticated (has valid token)
   */
  isAuthenticated(): boolean {
    return !!localStorage.getItem('JwtToken') && !this.isTokenExpired();
  }
}
