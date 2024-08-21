import { Component, Inject, inject, OnInit } from '@angular/core';
import { ModalLayoutComponent } from '../../modal/modal-layout/modal-layout.component';
import {
  FormBuilder,
  FormControl,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { TransactionRequest } from '../../../model/requests';
import { AccountInfo } from '../../../model/responses';
import { TransactionService } from '../../../services/transaction/transaction.service';
import { AccountService } from '../../../services/account/account.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';



@Component({
  selector: 'app-add-transaction',
  standalone: true,
  imports: [ModalLayoutComponent, ReactiveFormsModule, MatSnackBarModule],
  templateUrl: './add-transaction.component.html',
  styleUrl: './add-transaction.component.css',
})
export class AddTransactionComponent implements OnInit {
  _snackBar = inject(MatSnackBar);
  formBuilder = inject(FormBuilder);
  accountService = inject(AccountService);
  transactionService = inject(TransactionService);
  types: string[] = ['Expense', 'Income'];
  accounts: AccountInfo[] = [];
  accounts$: Observable<AccountInfo[]>;
  prefix: string = '0 ';

  form = this.formBuilder.group({
    amount: new FormControl('', Validators.required),
    description: new FormControl<string>('', Validators.required),
    accountId: new FormControl<string>('', Validators.required),
    type: new FormControl<string>('', Validators.required),
  });

  constructor(
    public dialogRef: MatDialogRef<ModalLayoutComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {
    this.accounts$ = this.accountService.getAllAccountInfo();
  }

  ngOnInit(): void {
    this.accounts$.subscribe({
      next: (res: AccountInfo[]) => {
        this.accounts = res;
      },
    });

    this.form.get('accountId')?.valueChanges.subscribe((id) => {
      if (id) {
        const prefix = '0 ';
        const currency = this.accounts.find((account) => account.id === id)
          ?.currency;
        this.form
          .get('amount')
          ?.setValue(prefix + currency, { emitEvent: false });
      }
    });
  }

  save() {
    if (this.form.valid) {
      let amount =
        this.extractNumberFromString(this.form.get('amount')?.value ?? '') ?? 0;
      const type = this.form.get('type')?.value;

      if (type === 'Expense' && amount > 0) {
        amount = -amount;
      } else if (type === 'Income' && amount < 0) {
        amount = Math.abs(amount);
      }

      const transactionReq: TransactionRequest = {
        amount: amount,
        description: this.form.get('description')?.value ?? '',
        accountId: this.form.get('accountId')?.value ?? '',
      };

      this.transactionService.create(transactionReq).subscribe({
        next: () => {
          console.log('created');
          this._snackBar.open('Transaction ' + transactionReq.description + ' created!', 'Close', {
            duration: 10000
          });
          this.form.reset();
          this.dialogRef.close(true);
          this.accountService.emitBalanceUpdated();
        },
        error: () => {
          this.dialogRef.close(false);
          this._snackBar.open('Something went wrong!', 'Close', {
            duration: 10000
          });
        },
      });
    } else {
      this._snackBar.open('Invalid data!', 'Close', {
        duration: 10000
      });
    }
  }

  extractNumberFromString(input: string): number | null {
    if (typeof input === 'string') {
      const match = input.match(/[\d.]+/);
      if (match) {
        console.log('mac je ' + match[0]);
        return parseFloat(match[0]);
      }
    } else {
      console.error('Input is not a string:', input);
    }
    return null;
  }
}
