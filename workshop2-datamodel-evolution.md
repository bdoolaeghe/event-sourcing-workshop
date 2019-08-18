Bank account data model evolution
=================================
*Goal: *

## Implement the currency in bank account
We want to make the following business enhancements:
* define a currency for a given account, that should be set when the account is opened by its owner.
* allow withdrawal in another currency, with an exchange rate (using the `CurrencyConverterService`)

Implement...
After that, the test `MoneyTransferWithCurrenciesTest` should pass green !

## Implement the fees on withdrawal with conversion
Now that we can do some money withdrawal in another currency, 
the bank service would like to deduce a 1% fee for such operations.

Implement...
The test `MoneyTransferWithCurrenciesTest` should pass green !

## Evolution on withdrawal fees computation
Mmh... Still not enough earned money for the bank... Hey ! 
let's change the fees computation for the future withdrawals, with this new rule:
* for withdrawal bigger than 10E, the fee is 1% of the amount.
* for any smaller withdrawal, the fee is fixed at 1E.
