**If you use GitHub permalinks, make sure your link points to the most recent commit before the milestone deadline.**

## Self-Evaluation Form for Milestone 9

Indicate below each bullet which file/unit takes care of each task.

Getting the new scoring function right is a nicely isolated design
task, ideally suited for an inspection that tells us whether you
(re)learned the basic lessons from Fundamentals I, II, and III. 

This piece of functionality must perform the following four tasks:

- find the player(s) that has(have) visited the highest number of goals
- compute their distances to the home tile
- pick those with the shortest distance as winners
- subtract the winners from the still-active players to determine the losers

The scoring function per se should compose these functions,
with the exception of the last, which can be accomplised with built-ins. 

1. Point to the scoring method plus the three key auxiliaries in your code. 

We used a GoalHandler class to handle all goal related task for our Referee:

https://github.khoury.northeastern.edu/CS4500-F22/salty-whales/blob/bfd31f85ef2f6534f49acc31c9aba9e0c4f7fbb6/Maze/Common/labyrinth/src/main/java/referee/GoalHandler.java#L17-L118

Our main scoring function on the referee:

https://github.khoury.northeastern.edu/CS4500-F22/salty-whales/blob/bfd31f85ef2f6534f49acc31c9aba9e0c4f7fbb6/Maze/Common/labyrinth/src/main/java/referee/Referee.java#L216-L246

Calculating the distance:

https://github.khoury.northeastern.edu/CS4500-F22/salty-whales/blob/bfd31f85ef2f6534f49acc31c9aba9e0c4f7fbb6/Maze/Common/labyrinth/src/main/java/model/Position.java#L70-L73

Our main scoring method holds the functionality of finding the players with the most goals and finding the closest player. We acknowledge this is improper design, but the code was in a less than optimal place and we prioritized getting a functioning program with the time we had.


3. Point to the unit tests of these four functions.

These are the tests regarding the details of our goal handler:
https://github.khoury.northeastern.edu/CS4500-F22/salty-whales/blob/bfd31f85ef2f6534f49acc31c9aba9e0c4f7fbb6/Maze/Common/labyrinth/src/test/java/referee/GoalHandlerTest.java#L103-L157

The ideal feedback for each of these three points is a GitHub
perma-link to the range of lines in a specific file or a collection of
files.

A lesser alternative is to specify paths to files and, if files are
longer than a laptop screen, positions within files are appropriate
responses.

You may wish to add a sentence that explains how you think the
specified code snippets answer the request.

If you did *not* realize these pieces of functionality, say so.

