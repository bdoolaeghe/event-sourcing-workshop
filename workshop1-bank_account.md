Bank account in an event-sourcing style
=======================================

*Goal: write your first simple event sourcing style application.*
* understand eventstore vs. state store
* understand hydratation
* FSM
                      
                   [deposit]
                      ||
(NEW)--[register]-->(OPEN)-->(CLOSED)
                      ||
                   [withraw]
