import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { apiEndpoint } from '../constants';
import { Transaction } from '../../model/models';
import { TransactionRequest } from '../../model/requests';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  http = inject(HttpClient);

  // getAmount(id: string): Observable<number> {
  //   return this.http.get<number>(`${apiEndpoint.TransactionEndpoint.getAmount}`);
  // }

  getAll(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${apiEndpoint.TransactionEndpoint.getAll}`);
  }

  getAllByAccount(id:string): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${apiEndpoint.TransactionEndpoint.getAll + id}`);
  }

  getAllFiltered(accountName: string, currentPage: number, pageSize: number): Observable<Transaction[]> {
    let params;
    console.log(accountName);
    if(accountName !== 'All'){
      params = new HttpParams().set('accountName', accountName).append('page', currentPage).append('size', pageSize);
    }else{
      params = new HttpParams().set('accountName',accountName).append('page', currentPage).append('size', pageSize);
    }
    return this.http.get<Transaction[]>(`${apiEndpoint.TransactionEndpoint.getAllFiltered}`, {params});
  }

  create(transactionRequest: TransactionRequest): Observable<Transaction>{
    console.log(transactionRequest.amount);
    return this.http.post<Transaction>(`${apiEndpoint.TransactionEndpoint.create}`, transactionRequest);
  }
  
}
