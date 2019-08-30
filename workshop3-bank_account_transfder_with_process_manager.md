Event-sourcing and Process Manager
==================================

*Goal: Understand the Process Manager*
      
## Process Manager
Sometimes, a business process can be much more complicated thant a fund transfer between 2 accounts:
* when it implies a lots of different actors (aggregates).
* when the process is long and composed of many steps (it implies many successive commands and evolutions)
* when you don't want to create coupling between aggregates;
 
For these reasons, you may want to delegate a process orchestration to a dedicated actor : a **Process Manager**.
Its role is to:
* define the **process orchestration** (translate a raised event into a decision function call on an aggregate).
* route the different messages between implied aggregates.
* **not** to perform any business logic.

      
## Transfer Process Manager

In the previous workshop3, we implemented the account transfer process onto the `Account` aggregate.
Let's train with a *Transfer Process Manager*. The goal of this workshop is to refactor the previous solution, introducing a `TransferProcessManager`.

The `TransferProcessManager` is a spring `EventListener` implementing callbacks on events occurring on the sender and receiver account.

Complete its implementation:
 
 ``` 
     @EventListener
     public void on(FundCredited fundCredited) {
        ...
     }
```
 
`BankCommandsTests.should_successfully_transfer()` should then pass green !


