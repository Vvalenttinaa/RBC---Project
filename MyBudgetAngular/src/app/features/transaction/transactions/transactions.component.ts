import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { concatMap, from, map, Observable, of, switchMap, toArray } from 'rxjs';
import { Transaction } from '../../../model/models';
import { AccountService } from '../../../services/account/account.service';
import { CurrencyService } from '../../../services/currency/currency.service';
import { TransactionService } from '../../../services/transaction/transaction.service';
import { UserService } from '../../../services/user/user.service';
import { AddTransactionComponent } from '../add-transaction/add-transaction.component';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, MatPaginatorModule],
  templateUrl: './transactions.component.html',
  styleUrl: './transactions.component.css',
})
export class TransactionsComponent implements OnInit {
  currencyService = inject(CurrencyService);
  userService = inject(UserService);
  dialog = inject(MatDialog);
  transactionService = inject(TransactionService);
  accountService = inject(AccountService);
  fb = inject(FormBuilder);

  transactions: Transaction[] = [];
  transactions$: Observable<Transaction[]>;
  userCurrency$: Observable<string>;
  userCurrency: string = '';

  options: string[] = ['All accounts'];
  filter: string = 'All accounts';
  totalPages: number = 0;
  pageSize: number = 5;
  currentPage: number = 0;
  form!: FormGroup;

  constructor() {
    this.transactions$ = this.transactionService.getAll();
    this.userCurrency$ = this.userService.getCurrencySub();
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      acc: new FormControl('All accounts'),
    });

    this.fetchTransactions(this.currentPage, this.pageSize).subscribe({
      next: (res: Transaction[]) => {
        this.transactions = res;
      },
    });

    this.getCurrency();
    this.getCurrencyNames();
    this.filterData();
  }

  getCurrency() {
    this.userCurrency$.subscribe({
      next: (res: string) => {
        this.userCurrency = res;
      },
    });
  }

  getCurrencyNames() {
    this.accountService.getAllNames().subscribe({
      next: (res: string[]) => {
        this.options.push(...res);
      },
    });
  }

  fetchTransactions(page: number, size: number): Observable<Transaction[]> {
    const fetchObservable =
      this.filter === 'All accounts'
        ? this.transactionService.getAllFiltered('All', page, size)
        : this.transactionService.getAllFiltered(this.filter, page, size);

    return fetchObservable.pipe(
      switchMap((transactions: Transaction[]) =>
        from(transactions).pipe(
          concatMap((transaction) => {
            if (
              transaction.accountCurrency.toLowerCase() !==
              this.userCurrency.toLowerCase()
            ) {
              return this.currencyService
                .convertToCurrency(
                  transaction.accountCurrency,
                  this.userCurrency,
                  transaction.amount,
                )
                .pipe(
                  map((convertedAmount: number) => {
                    transaction.userAmount = convertedAmount.toFixed(0);
                    return transaction;
                  }),
                );
            } else {
              transaction.userAmount = transaction.amount;
              return of(transaction);
            }
          }),
          toArray(),
        ),
      ),
    );
  }

  filterData() {
    this.form
      .get('acc')
      ?.valueChanges.pipe(
        switchMap((selectedValue) => {
          this.filter = selectedValue;
          this.currentPage = 0;
          return this.fetchTransactions(this.currentPage, this.pageSize);
        }),
      )
      .subscribe({
        next: (updatedTransactions: Transaction[]) => {
          console.log('Updated Transactions:', updatedTransactions);
          this.transactions = updatedTransactions;
        },
        error: (err) => {
          console.error('Error fetching or converting transactions:', err);
        },
      });
  }

  addTransaction() {
    const dialogRef = this.dialog.open(AddTransactionComponent, {
      width: '80vw',
      maxWidth: '600px',
      height: 'auto',
      maxHeight: '90vh',
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('closed ' + result);
      if (result) {
        this.fetchTransactions(this.currentPage, this.pageSize);
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.fetchTransactions(this.currentPage, this.pageSize).subscribe({
      next: (updatedTransactions: Transaction[]) => {
        this.transactions = updatedTransactions;
      },
      error: (err) => {
        console.error('Error fetching transactions:', err);
      },
    });
  }

  isEqual(accountCurrency: string, userCurrency: string): boolean {
    return accountCurrency.toLowerCase() === userCurrency.toLowerCase();
  }
}
