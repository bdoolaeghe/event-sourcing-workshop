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
### Nominal case
The `transfer` command needs to orchestrate 2 actions:
1. remove amount from source account.
2. add amount to target account. 

As the business logic orchestration is pretty simple, let's implement it in the *decision functions* of the `Account` aggregate.
Check the `command` implementation in `BankingService`:
```
    @Command
    public void transfer(AccountId idFrom, AccountId idTo, int amount) {
        final Account accountFrom = repository.load(idFrom);
        final Account accountTo = repository.load(idTo);
        accountFrom.requestTransfer(accountTo, amount);
        repository.save(accountFrom);
        repository.save(accountTo);
    } 
```


## How to manage failures ?

When something wrong is requested (e.g. withraw from a closed account), 
we decided until now to implement the failure with an exception.

Sometimes, it will be interesting  

## Commands
The command `BankingService.transfer(AccountId from, AccountId to, int amount)` is already implemented.

## Business events
The following business events have been identified:
* `AccountOpened`
* `AccountDeposited`
* `AccountWithdrawn`
* `AccountClosed`

## Implement AccountRepository
We are going to implement the `AccountRepository` based on an *eventStore*. 
A simple *eventStore* can be seen as a key/value store, where key is the *id* of the persisted aggregate (the *account* here), 
and the value is the stream of events that occurred on the aggregate, leading to its last state. 

In an eventStore:
* **saving** a new aggregate state means **storing all the business events** leading to the aggregate state.
* **loading** an aggregate in its last state means **loading all the previously stored events** for this aggregate,
and **apply** all of them on a "new born" aggregate (*hydratation*).
*NB: that means all stored events must contain all required data to reconstitute the state of the aggregate !*

## Implement the bank account
Now we have an `AccountRepository`, let's implement the bank account...

### Commands
The commands `openAccount` and `closeAccount` are already implemented in `BankingService`. 
You should now implement the 2 commands:
* `deposit`
* `withdraw`

### Decision functions
*Decision functions* are implementing the business logic invoked by a command.
In `Account` aggregate, you can have a look to the already implemented functions `create()`, `register()` and `close()` 
to implement the missing functions `deposit()` and `withdraw()`:
``` 
public class Account extends AggregateRoot<AccountId> {
   ...

  @DecisionFunction
  public Account deposit(Integer amount) {
     ...
  }

  @DecisionFunction
  public Account withdraw(Integer amount) {
     ...
  }
  ...
}
``` 

### Evolution functions
*Evolution functions* are responsible for mutating the aggregate state, given an input business `Event`.
As these functions are used to *hydrate* an aggregate (i.e. reload its state) using the event stream from the eventStore, they should be simple and contain no business logic and throw no business exception.

Check the already implemented functions `apply(AccountOpened accountOpened)` and `apply(AccountClosed accountClosed)` 
to Implement the evolution functions handling the `AccountDeposited` and `AccountWithdrawn` events:
```
    @EvolutionFunction
    void apply(AccountDeposited accountDeposited) {
       ...
    }

    @EvolutionFunction
    void apply(AccountWithdrawn accountWithdrawn) {
       ...
    } 
``` 

After that, all unit tests should pass green !
