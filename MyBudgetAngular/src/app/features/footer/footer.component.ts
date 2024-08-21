import { Component, inject, OnInit, signal } from '@angular/core';
import { AccountService } from '../../services/account/account.service';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { AddTransactionComponent } from '../transaction/add-transaction/add-transaction.component';
import { Observable, Subscription } from 'rxjs';
import { UserService } from '../../services/user/user.service';
import { Account, Currency } from '../../model/models';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [MatButtonModule, CommonModule],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.css',
})

export class FooterComponent implements OnInit {
  amount!: number;
  currency!: string;
  accountService = inject(AccountService);
  userService = inject(UserService);
  dialog = inject(MatDialog);
  totalBalance$: Observable<Currency>;
  private subscription = new Subscription();

  constructor() {
    this.totalBalance$ = this.accountService.getSumBalances();
  }

  ngOnInit(): void {
    this.userService.getCurrencySub().subscribe({
      next: (currency: string) => {
        this.currency = currency;
        this.getSum();
      },
      error: (err) => console.error('Error updating currency in footer:', err),
    });

    this.getSum();

    this.subscription.add(
      this.accountService.balanceUpdated$.subscribe(() => {
        this.refreshBalance();
      })
    );
  }

  getSum(){
    this.totalBalance$.subscribe({
      next: (res: Currency) => {
        this.amount = res.value;
        this.currency = res.name;
      },
    });
  }

  addTransaction() {
    this.dialog.open(AddTransactionComponent);
  }

  refreshBalance() {
    this.accountService.getSumBalances().subscribe(newBalance => {
      this.amount = newBalance.value;
    });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
