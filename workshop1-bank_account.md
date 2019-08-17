Bank account in an event-sourcing style
=======================================

*Goal: write your first simple event sourcing style application.*
* understand *eventstore* vs. *state store*
* implement *event stream saving* and aggregates *hydrating*
* implement a simple Bank acacount aggregate as a [Finite State Machine](https://fr.wikipedia.org/wiki/Automate_fini)
      
## Understand the bank account business
We are going to implement the following *bank account* use cases:
### Account creation
```gherkin
Given I am a customer
When I register for a new bank account
Then I become the owner of a new open account with a balance of 0.
```
### Deposit
```gherkin
Given I am the owner of an open account
When I deposit money on my account
Then my account balance is increased of the given deposit.
```
### Withdrawal
```gherkin
Given I am the owner of an open account
And my balance is 200
When I withdraw from my account an amount up to 200
Then my account balance is decreased of the given withdrawal.
```
### Withdrawal failure
```gherkin
Given I am the owner of an open account
And my balance is 200
When I withdraw from my account an amount greater than 200
Then the withdrawal is refused
And my account balance stay to 200.
```
### Account closing
```gherkin
Given I am the owner of an open account
When I close my account
Then my account is closed and no deposit/withdrawal is possible any more.
```
## Account states and transitions
A *bank account* can be seen as an aggregate with a *state*, on which we can apply some actions. 
Perfect to design as a [FSM](https://fr.wikipedia.org/wiki/Automate_fini) !
![bank account FSM](/assets/bank_account_fsm.png)                 

## Commands
Regarding to the previous use cases, The following commands have been identified:
* open an account
* deposit money
* withdraw money
* close an account

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
