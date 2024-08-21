import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { User } from '../../../model/models';
import { AccountService } from '../../../services/account/account.service';
import { CurrencyService } from '../../../services/currency/currency.service';
import { UserService } from '../../../services/user/user.service';
import { Observable, switchMap } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule, FormsModule, ReactiveFormsModule],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css',
})
export class SettingsComponent implements OnInit {
  _snackBar = inject(MatSnackBar);
  userService = inject(UserService);
  currencyService = inject(CurrencyService);
  accountService = inject(AccountService);
  user!: User;
  user$: Observable<User>;
  currencies$: Observable<string[]>;
  currencies: string[] = [];

  settingsForm = new FormGroup({
    currency: new FormControl(''),
  });

  constructor(){
    this.user$ = this.userService.getUser();
    this.currencies$ = this.currencyService.getAll();
  }

  ngOnInit(): void {
    
    this.currencies$.subscribe({
      next:(res: string[]) => {
        this.currencies=res;
      }
    });

    this.user$.subscribe({
      next:(res:User)=>{
        this.user=res;
        this.settingsForm.get('currency')?.setValue(this.user.currentCurrency);
      }
    });

    this.settingsForm.get('currency')?.valueChanges.subscribe((currency) => {
      if (currency) {
        this.updateCurrency(currency);
      }
    });
  }

  updateCurrency(currency: string) {
    this.userService.updateCurrency(currency).pipe(
      switchMap((res: User) => {
        this.userService.updateCurrencySub(currency);
        return this.userService.getCurrencySub(); 
      })
    ).subscribe({
      next: (updatedCurrency: string) => {
        console.log('Currency after update:', updatedCurrency);
        this._snackBar.open('Currency updated sucessfully!', 'Close', {
          duration: 10000
        });
      },
      error: (err) =>{ console.error('Error updating currency:', err);
        this._snackBar.open('Something went wrong!', 'Close', {
          duration: 10000
        });
      }
    });
  }

  deleteAll(){
    this.accountService.deleteAll().subscribe({
      next:(res:any) => {
        console.log('deleted');
        this._snackBar.open('All data deleted!', 'Close', {
          duration: 10000
        });
      },
      error: ()=>{
        console.log('Error');
        this._snackBar.open('Something went wrong!', 'Close', {
          duration: 10000
        });
      }
    });
  }
}
