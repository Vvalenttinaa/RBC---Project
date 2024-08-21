export interface AccountRequest {
  name: string;
  currency: string;
  balance: number;
}

export interface TransactionRequest {
  amount: number;
  description: string;
  accountId: string;
}

export interface CurrencyConversionRequest {
  from: string;
  to: string;
  amount: number;
  currency: string
}
