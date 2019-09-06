Event-sourcing and aggregate messages
=====================================

*Goal: Understand how aggregates can communicate together in a simple business process.*
      
## Transfer from an account to another
We are going to implement the *Transfer* feature. The *Transfer* process implies a *sender account* and a *receiver account*. 
The given *amount* should be deduced from *sender account* and added to the *receiver account* 
### Succeeding transfer
```gherkin
Given a sender account "A" and a receiver account "B"
And "A" has a balance of 200
And "B" has a balance of 0
When I transfer 50 from "A" to "B"
Then "A" balance is 150
Then "B" balance is 50
```
### Transfer with insufficient funds
```gherkin
Given a sender account "A" and a receiver account "B"
And "A" has a balance of 10
And "B" has a balance of 0
When I transfer 50 from "A" to "B"
Then "A" balance is 10
Then "B" balance is 0
```
### Transfer to a closed account
```gherkin
Given a sender account "A" and a receiver account "B"
And "A" has a balance of 200
And "B" is closed
When I transfer 50 from "A" to "B"
Then "A" balance is 200
```

## Implementation

The `requestTransfer` command needs to orchestrate 2 actions:
1. debit amount from source account.
2. credit amount to target account. 

The business logic orchestration is pretty simple, and is implemented it in the `Account` aggregate *decision functions*.
Check how the `requestTransfer()` orchestrate the debit and credit on the sender and receiver account. 
When an action occurs to roll the aggregate state, we keep an event for it. 
Note that the business logic is in *decision functions* only. Once again, *evolution functions* only change the state of aggregate.


## Missing implementation

Implement the missing code in `Account` aggregate:

### Fix insufficient funds case

When funds are insufficient for a requested transfer, the transfer request should be refused. 
Fix `Account.requestTransfer()`: 
```
  @DecisionFunction
    public Account requestTransfer(Account receiverAccount, int amount) {
        ...
        if (amount <= getBalance()) {
            ...
        } else {
            //FIXME when funds are insufficient...
            // apply a TransferRequestRefused evolution on sender account
            throw new RuntimeException("implement me !");
        }
        ...
    }
```
The test `BankCommandTest.should_fail_transfer_when_funds_are_insufficient()` should then pass green !

### Fix the credit() fucntion

When a transfer is requested, the `credit()` decition function is invoked on the receiver account by the sender account.
Implement this function:
```
    @DecisionFunction
    public void credit(Account senderAccount, int amount) {
        //FIXME expected implementation:
        // IF the receiver account is OPEN
        // 1. apply a FundCredited evolution on receiver account
        // 2. make debit() decition on sender account
        // ELSE
        // 1. apply a CreditRequestRefused evolution on receiver account
        // 2. make abortTransferRequest() decision on sender account
        throw new RuntimeException("implement me !");
    }
```
After that, all the tests should pass green !
