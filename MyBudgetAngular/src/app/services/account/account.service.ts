import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { apiEndpoint } from '../constants';
import { Account, Currency } from '../../model/models';
import { AccountRequest } from '../../model/requests';
import { AccountInfo } from '../../model/responses';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  http = inject(HttpClient);
  private balanceUpdatedSubject = new Subject<void>();
  balanceUpdated$ = this.balanceUpdatedSubject.asObservable();

  emitBalanceUpdated() {
    this.balanceUpdatedSubject.next();
  }

  getSumBalances(): Observable<Currency> {
    return this.http.get<Currency>(`${apiEndpoint.AccountEndpoint.getAmount}`);
  }

  getAll(page: number, size: number): Observable<Account[]> {
    let params = new HttpParams().set('page', page).append('size', size);
    return this.http.get<Account[]>(`${apiEndpoint.AccountEndpoint.getAll}`, {params});
  }

  getAllNames(): Observable<string[]>{
    return this.http.get<string[]>(`${apiEndpoint.AccountEndpoint.getAllNames}`);
  }

  getAllAccountInfo(): Observable<AccountInfo[]>{
    return this.http.get<AccountInfo[]>(`${apiEndpoint.AccountEndpoint.getAllInfo}`);
  }

  create(accountRequest: AccountRequest): Observable<AccountRequest>{
    return this.http.post<AccountRequest>(`${apiEndpoint.AccountEndpoint.create}`, accountRequest);
  }

  getCurrency(id:string): Observable<string>{
    return this.http.get<string>(`${apiEndpoint.AccountEndpoint.getCurrency}` + id);
  }

  deleteAll(): Observable<void>{
    return this.http.delete<void>(`${apiEndpoint.AccountEndpoint.delete}`);
  }

}
