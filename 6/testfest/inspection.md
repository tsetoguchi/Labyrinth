Pair: recursive-koodotk \
Commit: [3ca3c9200a7b0e5523e0bab569813fa0d50e3037](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/tree/3ca3c9200a7b0e5523e0bab569813fa0d50e3037) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/a6448a989539083707d0d2ac59ef7d150b556a9c/6/self-6.md \
Score: 90/165 \
Grader: Varsha Ramesh

# Observer Run

[20/60] 

- [10/10] program runs
- [10/10] easy understanding of displayed state (player location, player home, spare)
- [0/10] clicking through entire game via next
- [0/10] saving the first game state in a file of choice 
- [0/10] can cancel save functionality without crashing program
- [0/10] confirming saved file is same as the RefereeState in the instructor's input JSON

# Programming

[50/60]

- [10/10] Observer interface with send_state functionality
- [10/10] Observer interface with end functionality
- [20/20] essential calls in Referee - sending initial state, sending state after turn, signaling no more states will be sent.
- [10/20] single point of control for whether the observer is informed
    - You have factored out your code out into two helper methods, but you still have 2 if checks, find a way to do it without it.

  
# Design

[20/45]

- [0/15] Every referee call from the player goes across the wire (remote proxy)
  - No diagram, did not describe the remote proxy pattern, and did not mention messages going across the wire.
- [15/15] Every call's arguments and results have a JSON representation
- [5/15] Specifies how client-players sign up with server 
  - It is not the job of the referee to handle player signups

