import { Component, Inject, inject } from '@angular/core';
import { ModalLayoutComponent } from '../../modal/modal-layout/modal-layout.component';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CurrencyService } from '../../../services/currency/currency.service';
import { AccountService } from '../../../services/account/account.service';
import { AccountRequest } from '../../../model/requests';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-add-account',
  standalone: true,
  imports: [ModalLayoutComponent, ReactiveFormsModule, MatFormFieldModule],
  templateUrl: './add-account.component.html',
  styleUrl: './add-account.component.css',
})
export class AddAccountComponent {
  private _snackBar = inject(MatSnackBar);

  currencyService = inject(CurrencyService);
  accountService = inject(AccountService);
  accountForm!: FormGroup;
  currencies: string[] = [];
  currencies$: Observable<string[]>;
  prefix: string = '0 ';

  constructor(
    public dialogRef: MatDialogRef<AddAccountComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.currencies$ = this.currencyService.getAll();
  }

  ngOnInit() {
    this.accountForm = new FormGroup({
      name: new FormControl('', [
        Validators.required,
        Validators.maxLength(100),
      ]),
      currency: new FormControl('', [Validators.required]),
      balance: new FormControl('', [Validators.required]),
    });

    this.currencies$.subscribe({
      next: (res: string[]) => {
        this.currencies = res;
      },
    });

    this.accountForm.get('currency')?.valueChanges.subscribe((currency) => {
      if (currency) {
        const prefix = '0 ';
        this.accountForm
          .get('balance')
          ?.setValue(prefix + currency, { emitEvent: false });
      }
    });
  }

  save() {
    const balanceControl = this.accountForm.get('balance');
    if (balanceControl) {
      const number = this.extractNumberFromString(balanceControl.value);
      if (number !== null) {
        balanceControl.setValue(number, { emitEvent: false });
      }
    }

    if (this.accountForm.valid) {
      const accountRequest: AccountRequest = this.accountForm.value;
      this.accountService.create(accountRequest).subscribe({
        next: () => {
          console.log('created');
          this._snackBar.open('Account ' + accountRequest.name + ' created!', 'Close', {
            duration: 10000
          });
          this.accountForm.reset();
          this.dialogRef.close(true);
          this.accountService.emitBalanceUpdated();
        },
        error: ()=>{
          this._snackBar.open('Something went wrong!', 'Close', {
            duration: 10000
          });
          this.dialogRef.close(false);
        }
      });
    } else {
      this._snackBar.open('Invalid data!', 'Close', {
        duration: 10000
      });
    }
  }

  extractNumberFromString(input: string): number | null {
    if (typeof input === 'string') {
      const match = input.match(/\d+/);
      if (match) {
        return parseInt(match[0], 10);
      }
    } else {
      console.error('Input is not a string:', input);
    }
    return null;
  }
}
