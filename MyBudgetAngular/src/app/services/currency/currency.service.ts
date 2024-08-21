import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { apiEndpoint } from '../constants';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  http = inject(HttpClient);
  private cache$ = new BehaviorSubject<any>(null);

  getAll(): Observable<string[]> {
    if (!this.cache$.value) {
      return this.http.get<string[]>(`${apiEndpoint.CurrencyEndpoint.getAll}`)
      .pipe(
        tap(data => this.cache$.next(data))
      );
    } else {
      return this.cache$.asObservable();
    }
  }

  convertToCurrency(from: string, to: string, amount: string): Observable<number>{
    let params = new HttpParams().set('from', from).append('to', to).append('amount', amount);
    return this.http.get<number>(`${apiEndpoint.CurrencyEndpoint.conversion}`, {params});
  }

}
