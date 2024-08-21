import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable, OnInit, signal } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { apiEndpoint } from '../constants';
import { User } from '../../model/models';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  http = inject(HttpClient);
  private currencySubject = new BehaviorSubject<string>('');
  currentCurrency$ = this.currencySubject.asObservable();
  
  constructor() {
    this.getCurrency().subscribe({
      next: (res: string) => {
        this.currencySubject.next(res);
      },
      error: (err) => console.error('Error fetching initial currency:', err)
    });
  }

  getUser(): Observable<User> {
    return this.http.get<User>(`${apiEndpoint.UserEndpoint.getUser}`);
  }

  getCurrency(): Observable<string> {
    return this.http.get<string>(`${apiEndpoint.UserEndpoint.getCurrency}`, { responseType: 'text' as 'json' });
  }

  updateCurrency(currency: string): Observable<User> {
    return this.http.put<User>(`${apiEndpoint.UserEndpoint.updateCurrency}`, currency);
  }

  updateCurrencySub(newCurrency: string): void {
    this.currencySubject.next(newCurrency);
  }

  getCurrencySub(): Observable<string> {
    return this.currentCurrency$;
  }

}
