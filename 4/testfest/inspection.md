Pair: recursive-koodotk \
Commit: [71527df2dc879ca6158ca497218cb3e8e0e57084](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/tree/71527df2dc879ca6158ca497218cb3e8e0e57084) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/564376411cf0f0eac2686b9b139fc1b62f2e8b16/4/self-4.md \
Score: 97/110 \
Grader: Alexis Hooks

<hr>

SELF EVAL **[7/10]**:

- [-3] - helpful and accurate self evaluation
  - Your self eval linked to a commit that was pushed after the milestone deadline (no significant changes were made so i'm only deducted 3 pts). Next time link to a pre-deadline commit

<hr>

PROGRAMMING **[90/100]**

- [20/20] - Data definition for the return value of a call to strategy
- [15/15] - a good name, signature/type, and purpose statement for a method that <em>composes</em> `generating` a sequence of possible locations to move to and `searching` through those locations to find the best one
- [15/15] - a good name, signature/type, and purpose statement for `generating` a sequence of spots a player may wish to move to
- [10/15] - a good name, signature/type, and purpose statement for `searching` rows <em>then</em> columns 
  - [-5] - not mentioned in the purpose statement how the search is conducted (i.e. first rows, then columns)
  - this is a very long method (split into helpers)
- [10/15] - a good name, signature/type, and purpose statement for a function/method that makes sure the player's location is (1) not the target because the player <em>must</em> move to a different tile (2) is still able to reach the target
  - [+5] - checking that the target is still reachable after a slide/insert
  - [+5] - checking that after slide/insert the location of the player is not the goal/target tile
  - [-5] - factoring this out into a separate method
  - WARNING: your strategies don't ensure that a player doesn't undo a move

- [10/10] - unit test that produces an action to move the player
- [10/10] - unit test that forces the player to pass on its turn

<hr>

DESIGN TASK (ungraded):

- once: set up phase
- many times: taking turns
- once: ending the game

Linked to post-deadline commit ec75995a2e4df11b90a951f3fd54757bd2b9e24c from 10-21 12:39 PM
