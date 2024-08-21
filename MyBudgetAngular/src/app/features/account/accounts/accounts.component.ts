import { Component, inject, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import {
  catchError,
  from,
  map,
  mergeMap,
  Observable,
  of,
  Subject,
  switchMap,
  toArray,
} from 'rxjs';
import { Account } from '../../../model/models';
import { AccountService } from '../../../services/account/account.service';
import { CurrencyService } from '../../../services/currency/currency.service';
import { UserService } from '../../../services/user/user.service';
import { AddAccountComponent } from '../add-account/add-account.component';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [MatPaginatorModule, CommonModule],
  templateUrl: './accounts.component.html',
  styleUrl: './accounts.component.css',
})
export class AccountsComponent implements OnInit {
  _snackBar = inject(MatSnackBar);
  balanceUpdated$ = new Subject<void>();
  accountService = inject(AccountService);
  userService = inject(UserService);
  currencyService = inject(CurrencyService);
  dialog = inject(MatDialog);
  accounts: Account[] = [];
  accounts$: Observable<Account[]>;
  userCurrency$: Observable<string>;
  userCurrency: string = '';

  totalPages: number = 0;
  pageSize: number = 5;
  currentPage: number = 0;

  constructor() {
    this.userCurrency$ = this.userService.getCurrencySub();
    this.accounts$ = this.accountService.getAll(
      this.currentPage,
      this.pageSize,
    );
  }

  ngOnInit(): void {
    this.fetchAccounts(this.currentPage, this.pageSize);
    this.accountService.balanceUpdated$.subscribe(() => {
      this.fetchAccounts(this.currentPage, this.pageSize);
    });
  }

  fetchAccounts(page: number, size: number): void {
    this.userCurrency$
      .pipe(
        switchMap((userCurrency) => {
          this.userCurrency = userCurrency;
          return this.accountService.getAll(page, size).pipe(
            mergeMap((accounts) =>
              from(accounts).pipe(
                mergeMap((account) => {
                  if (account.currency !== this.userCurrency) {
                    return this.currencyService
                      .convertToCurrency(
                        account.currency,
                        this.userCurrency,
                        account.balance.toString(),
                      )
                      .pipe(
                        map((convertedValue) => ({
                          ...account,
                          convertedAmount: convertedValue,
                        })),
                        catchError((err) => {
                          console.error('Error converting currency:', err);
                          this._snackBar.open('Something went wrong!', 'Close', {
                            duration: 10000
                          });
                          return of({
                            ...account,
                            convertedAmount: account.balance,
                          });
                        }),
                      );
                  } else {
                    return of({
                      ...account,
                      convertedAmount: account.balance,
                    });
                  }
                }),
                toArray(),
              ),
            ),
          );
        }),
      )
      .subscribe((convertedAccounts) => {
        this.accounts = convertedAccounts;
      });
  }

  addAccount() {
    const dialogRef = this.dialog.open(AddAccountComponent, {
      width: '80vw',
      maxWidth: '600px',
      height: 'auto',
      maxHeight: '90vh',
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log('closeed ' + result);
      if (result) {
        this.fetchAccounts(this.currentPage, this.pageSize);
        this.balanceUpdated$.next();
      }
    });
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.fetchAccounts(this.currentPage, this.pageSize);
  }

  isEqual(accountCurrency: string, userCurrency: string): boolean {
    return accountCurrency.toLowerCase() === userCurrency.toLowerCase();
  }
}
