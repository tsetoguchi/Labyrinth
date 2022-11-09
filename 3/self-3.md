## Self-Evaluation Form for Milestone 3

Indicate below each bullet which file/unit takes care of each task:

1. rotate the spare tile by some number of degrees \
[slideAndInsert](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/31e96aa41bce933b19721db5318177cda89e8755/Maze/Common/labyrinth/src/main/java/game/model/Game.java#L40-L46)
2. shift a row/column and insert the spare tile \
[slideAndInsert](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/31e96aa41bce933b19721db5318177cda89e8755/Maze/Common/labyrinth/src/main/java/game/model/Game.java#L40-L46)
   - plus its unit tests \
[unit tests](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/31e96aa41bce933b19721db5318177cda89e8755/Maze/Common/labyrinth/src/test/java/game/model/GameTest.java#L66-L181)

3. move the avatar of the currently active player to a designated spot \
We did not implement this, as it was not mentioned in the spec for Milestone 3.
We do have the feature that was required, which is determining if a tile is reachable: \
[activePlayerCanReachPosition](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/31e96aa41bce933b19721db5318177cda89e8755/Maze/Common/labyrinth/src/main/java/game/model/Game.java#L48-L53)

4. check whether the active player's move has returned its avatar home \
[activePlayerHasReachedGoal](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/31e96aa41bce933b19721db5318177cda89e8755/Maze/Common/labyrinth/src/main/java/game/model/Game.java#L55-L60)

5. kick out the currently active player \
[kickActivePlayer](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/31e96aa41bce933b19721db5318177cda89e8755/Maze/Common/labyrinth/src/main/java/game/model/Game.java#L68-L77)

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

