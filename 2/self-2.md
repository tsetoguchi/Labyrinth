## Self-Evaluation Form for Milestone 2

Indicate below each bullet which file/unit takes care of each task:

1. point to the functinality for determining reachable tiles 

We define a Board interface, which contains a [getReachablePositions method](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/4ebbb02e922693dd6b69cbe95485d6e61796581c/Maze/Common/labyrinth/src/main/java/game/board/Board.java#L11)
Implemented by StandardBoard [here](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/4ebbb02e922693dd6b69cbe95485d6e61796581c/Maze/Common/labyrinth/src/main/java/game/board/StandardBoard.java#L36)

   - a data representation for "reachable tiles" \
We represent the data as a `Set<Position>`, which are the position of the reachable tiles.
   - its signature and purpose statement \
Signature is the type signature and purpose statement is Javadoc [here](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/4ebbb02e922693dd6b69cbe95485d6e61796581c/Maze/Common/labyrinth/src/main/java/game/board/StandardBoard.java#L33-L36)
   - its "cycle detection" coding (accumulator) \
We keep a set of explored positions and check against it [here](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/4ebbb02e922693dd6b69cbe95485d6e61796581c/Maze/Common/labyrinth/src/main/java/game/board/StandardBoard.java#L45-L50)
   - its unit test(s) \
[Unit test](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/4ebbb02e922693dd6b69cbe95485d6e61796581c/Maze/Common/labyrinth/src/test/java/game/board/StandardBoardTest.java#L60)

2. point to the functinality for shifting a row or column 

   - its signature and purpose statement \
     Signature is the type signature and purpose statement is Javadoc [here](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/4ebbb02e922693dd6b69cbe95485d6e61796581c/Maze/Common/labyrinth/src/main/java/game/board/StandardBoard.java#L69)
   - unit tests for rows and columns \
[Unit tests](https://github.khoury.northeastern.edu/CS4500-F22/recursive-koodotk/blob/4ebbb02e922693dd6b69cbe95485d6e61796581c/Maze/Common/labyrinth/src/test/java/game/board/StandardBoardTest.java#L87-L181)

3. point to the functinality for inserting a tile into the open space

   - its signature and purpose statement
   - unit tests for rows and columns

We combined the functionality of these two functions, as keeping them separate would require intermediate state of the
"floating" game piece and an empty tile, and since a turn must involve both including the functionality together was
cleaner.

If you think the name of a method/function is _totally obvious_,
there is no need for a purpose statement. 

The ideal feedback for each of these points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

