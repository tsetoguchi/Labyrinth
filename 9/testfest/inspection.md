Pair: salty-whales \
Commit: [bfd31f85ef2f6534f49acc31c9aba9e0c4f7fbb6](https://github.khoury.northeastern.edu/CS4500-F22/salty-whales/tree/bfd31f85ef2f6534f49acc31c9aba9e0c4f7fbb6) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/salty-whales/blob/db453bbf5eac8135b1c8bf5591053280e1a89dee/9/self-9.md \
Score: 62/100 \
Grader: Alexis Hooks

<hr>

#### SELF EVALUATION [20/20]

[20/20] - accurate and helpful self eval

<hr>

#### PROGRAM INSPECTION [42/80]

##### Expectations

The top-level scoring function must perform the following tasks:

- find the player(s) that has(have) visited the highest number of goals
- compute their distances to the home tile
- pick those with the shortest distance as winners
- subtract the winners from the still-active players to determine the losers
  
The first three tasks should be separated out as methods/functions:

1. Find player(s) that has(have) visited the highest number of goals.
2. Compute player distances to next goal.
3. Find player(s) with shortest distance to next goal.

##### Points

- [22/40] Each of these functions should have a good name or a purpose statement
  - [0/10] top-level scoring function
    - your top level function should perform the task of subtracting the winners to determine game losers
  - [6/10] find the player(s) that has(have) visited the highest number of goals
    - partial credit for honesty
  - [10/10] compute player distances to the home tile
  - [6/10] pick players with the shortest distance as winners
    - partial credit for honesty

- [20/40] Each of these functions should have unit tests
  - [20/20] unit tests for scoring method
  - [0/10] for a unit test that covers no players in the game at the time of scoring OR purpose statements / data interpretations indicate the state comes with at least one active player
  - [0/10] or a unit test that covers scoring a game where multiple players have the same number of goals
  
<hr>

GENERAL NOTE:
- Make sure to obey the changes to the spec: "If the game-terminating player is one of the players with the highest number of collected treasures, it is the sole winner."

