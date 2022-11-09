**If you use GitHub permalinks, make sure your links points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 4

The milestone asks for a function that performs six identifiable
separate tasks. We are looking for four of them and will overlook that
some of you may have written deep loop nests (which are in all
likelihood difficult to understand for anyone who is to maintain this
code.)

Indicate below each bullet which file/unit takes care of each task:

1. the "top-level" function/method, which composes tasks 2 and 3 \
[interface](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/main/java/player/Strategy.java#L18) \
[creatTurnPlan](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/main/java/player/SimpleCandidateStrategy.java#L34-L45)

2. a method that `generates` the sequence of spots the player may wish to move to \
[getCandidatesInOrder](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/main/java/player/SimpleCandidateStrategy.java#L26) \
[Euclidean implementation](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/main/java/player/EuclideanStrategy.java#L25-L59) \
[Reimann implementation](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/main/java/player/ReimannStrategy.java#L20-L33)

3. a method that `searches` rows,  columns, etcetc.  \
[createTurnPlanForCandidate](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/main/java/player/SimpleCandidateStrategy.java#L63-L104)

4. a method that ensure that the location of the avatar _after_ the
   insertion and rotation is a good one and makes the target reachable \
[interface](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/main/java/game/model/ExperimentationBoard.java#L17) \
[standard implementation](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/main/java/game/model/StandardExperimentationBoard.java#L45-L56)

ALSO point to

- the data def. for what the strategy returns \
[TurnPlan](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/main/java/player/TurnPlan.java#L9-L37)

- unit tests for the strategy \
[Euclidean strategy tests](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/test/java/game/player/ReimannStrategyTest.java) \
[Reimann strategy tests](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/ec75995a2e4df11b90a951f3fd54757bd2b9e24c/Maze/Common/labyrinth/src/test/java/game/player/ReimannStrategyTest.java)

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality or realized
them differently, say so and explain yourself.


