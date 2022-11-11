**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 7

Indicate below each bullet which file/unit takes care of each task:

The require revision calls for

    - the relaxation of the constraints on the board size
    https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/f99feb6318be0fa2d73b54d6ea7e36206b456693/Maze/Common/labyrinth/src/main/java/game/model/Board.java#L15-L23
    This is the method that enforces the validation of whether a row or column is slidable. These interface methods are designed such that they do not enforce a specific Board size. However, we currently do not have a Board class that allows for a generic Board of any size to be constructed.
    
    - a suitability check for the board size vs player number 
https://github.khoury.northeastern.edu/CS4500-F22/plucky-bees/blob/f99feb6318be0fa2d73b54d6ea7e36206b456693/Maze/Common/labyrinth/src/main/java/game/model/Game.java#L234-L236
Although this code fragment does not directly enforce the suitability check for Player count and Board size, it does enforce unique homes for the Players in the Game. Therefore, the suitability check is implicit because if there are too many Players for the Board, there will not be enough Tiles for all of the Players to have their own unique home. Currently, the exception is commented out because this suitability check was undesired for this week's integration tests.

1. Which unit tests validate the implementation of the relaxation?
We currently do not have any unit tests to validate the relaxated constraints.

2. Which unit tests validate the suitability of the board/player combination? 
We currently do not have any unit tests to validate the suitability of board/player combination.
   
The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

