import { Routes } from '@angular/router';
import { AccountsComponent } from './features/account/accounts/accounts.component';
import { WrapperComponent } from './features/wrapper/wrapper.component';
import { TransactionsComponent } from './features/transaction/transactions/transactions.component';
import { SettingsComponent } from './features/settings/settings/settings.component';

export const routes: Routes = [
  {
    path: '',
    component: WrapperComponent,
    title: 'MyBudget',
    children: [
      {
        path: '',
        redirectTo: 'accounts',
        pathMatch: 'full',
      },
      {
        path: 'accounts',
        component: AccountsComponent,
        title: 'Accounts',
      },
      {
        path: 'transactions',
        component: TransactionsComponent,
        title: 'Transactions'
      },
      {
        path: 'settings',
        component: SettingsComponent,
        title: 'Settings'
      }
    ],
  },
];
