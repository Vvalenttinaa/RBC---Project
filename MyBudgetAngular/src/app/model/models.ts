export interface Account{
    id: string,
    name: string,
    currency: string,
    balance: number,
    deleted: boolean,
    convertedAmount: number
}

export interface Transaction{
    id: string,
    amount: string,
    description: string,
    accountId: string,
    accountName: string,
    deleted: boolean,
    accountCurrency: string,
    userAmount: string
}

export interface User{
    id: string,
    username: string,
    updatedAt: string,
    currentCurrency: string,
    // accountsById: Account[],
}

export interface Currency{
    name: string,
    value:number
}