Pair: recursive-koodotk \
Commit: [44e78ed1d32987cc376673e11e60cf965416a547](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/tree/44e78ed1d32987cc376673e11e60cf965416a547) \
Self-eval: https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/44e78ed1d32987cc376673e11e60cf965416a547/5/self-5.md \
Score: 116/160 \
Grader: Megan Li

# Self-Eval

- [14/20] for a helpful and accurate self-eval 
  - [-6] Misdirect on self-eval for getting the name from a player.

# Programming

[72/110]

## Player

[36/50]

Names, signatures/types, purpose statements for:

- [0/10] `name`
  - `PlayerAvatar` is NOT the player.
- [10/10] `propose board`
- [6/10] `setting up`
  - Does not exist but said so on self-eval. 
- [10/10] `take a turn`
- [10/10] `did I win`

Notes / feedback:
- Why do you have a `PlayerClient` and a `Player`? What's the difference between them? This is a genuine question because you don't have purpose statements for these two interfaces.
- Your player API does not obey the Player API in the spec.

## Referee

[16/40]

The _testing referee_ must perform the following tasks in order and hence must have separate functions:

- [6/10] setting up the player with initial information
  - Does not exist and said so on self-eval.
- [5/10] running rounds until the game is over
  - Partial credit because the while loop is very easy to read; HOWEVER:
  - Running rounds should be factored out into its own method (separate from determining winners / informing players of results) so there should be one top-level method in the referee that composes the multiple tasks a referee must perform.
  - Running rounds is different from running turns. Your while loop is running turns. See point below:
- [0/10] running a round
  - Your code factors out running a _turn_ but there must be a method to run a _round_ because one of the conditions for the game ending is "every player chooses not to move during one round". This is a different condition than `n` players consecutively passing.
- [5/10] determine the winners
  - This is a very long method with no purpose statement explaining how winners are determined.

Notes / feedback:
- The interactions between your referee and the players violate the Architect's protocol.
- There should be a referee constructor that just takes a list of players.
- Every referee call on the player should be protected against illegal behavior and infinite loops/timeouts/exceptions.

## Tests
[20/20]

- [10/10] a unit test for the referee function that returns a unique winner
- [10/10] a unit test for the scoring function that returns several winners

# Design

[30/30]

Describes gestures for:
- [10/10] rotates the tile before insertion
- [10/10] selects a row or column to be shifted and in which direction
- [10/10] selects the next place for the player's avatar
