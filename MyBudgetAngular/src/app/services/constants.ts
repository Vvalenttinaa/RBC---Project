const apiurl = 'http://localhost:8080';

export const apiEndpoint = {
  AccountEndpoint: {
    getAmount: `${apiurl}/account/total`,
    getAll: `${apiurl}/account`,
    getAllNames: `${apiurl}/account/name`,
    getAllInfo: `${apiurl}/account/info`,
    create: `${apiurl}/account`,
    getCurrency: `${apiurl}/account/currency/`,
    delete: `${apiurl}/account`,
  },
  TransactionEndpoint: {
    getAllByAccount: `${apiurl}/transaction/`,
    getAll:  `${apiurl}/transaction`,
    getAllFiltered: `${apiurl}/transaction/transactionsFiltered`,
    create: `${apiurl}/transaction`
  },
  UserEndpoint: {
    getUser: `${apiurl}/user`,
    updateCurrency: `${apiurl}/user/currency`,
    getCurrency: `${apiurl}/user/currency`
  },
  CurrencyEndpoint: {
    conversion: `${apiurl}/currency/conversion`,
    getAll: `${apiurl}/currency`
  }
}