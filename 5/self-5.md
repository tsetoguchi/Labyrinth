**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 5

Indicate below each bullet which file/unit takes care of each task:

The player should support five pieces of functionality: 

- `name` \
[PlayerAvatar.name](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/main/java/game/model/PlayerAvatar.java#L9)
- `propose board` (okay to be `void`) \
[proposeBoard](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/main/java/player/Player.java#L14)
- `setting up` \
We do not send this information initially, but we store all the requisite information in [PlayerGameView](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/main/java/game/view/PlayerGameView.java)
and send it in [takeTurn](https://g`ithub.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/main/java/referee/clients/PlayerClient.java#L13)
- `take a turn` \
[createTurnPlan](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/main/java/player/Player.java#L16)
- `did I win` \
[PlayerClient.informGameEnd](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/main/java/referee/clients/PlayerClient.java#L17)

Provide links. 

The referee functionality should compose at least four functions:

- setting up the player with initial information \
Our referee implementation accepts a list of initialized PlayerClients, but does
not deal with setting them up itself. The state information is sent to each player
when requesting a move: [takeTurn](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/main/java/referee/clients/PlayerClient.java#L13)
- running rounds until termination \
[runGame](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/main/java/referee/Referee.java#L53-L60)
- running a single round (part of the preceding bullet) \
[handleTurn](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/main/java/referee/Referee.java#L65-L87)
- scoring the game \
[getWinners](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/main/java/referee/Referee.java#L132-L167)

Point to two unit tests for the testing referee:

1. a unit test for the referee function that returns a unique winner \
[test1000Rounds](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/test/java/game/referee/RefereeTest.java#L58-L99)
2. a unit test for the scoring function that returns several winners \
[testAllSkip](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/b16c68fd5a7ddc5053f8f5205010c68c18ea158d/Maze/Common/labyrinth/src/test/java/game/referee/RefereeTest.java#L23-L55)

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files -- in the last git-commit from Thursday evening. 

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

