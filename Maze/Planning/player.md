<p style="text-align: center;">Business Memorandum</p>

TO: Ben Lerner

FROM: Kyle Koo and Duncan Vogel

DATE: 10/11/2022

SUBJECT: Design for Player functionality in Labyrinth

Now that the game state design is complete, we are designing the functionality that will be required for players
of our game to act. There are two actions that a player will take during a game (after registration and game start and
before game end and prizes): sliding a row, which includes inserting a tile to fill the empty space, and moving its
game piece.

For the Player, we have designed the following data representations, based on the data representations of game state: 

*BoardView*: \
contains a TileView[][] tiles, a read-only view of the tile grid \
contains a TileView spareTile, a read-only view of the spare tile 

*TileView*: \
contains booleans up, down, left, right, which represent whether the pathway on the tile conects to the edge in the
given direction. \
contains the Treasure on the tile. 

*SlideAndInsertAction*: \
contains boolean pass, which represents whether the player has passed \
if pass == false: \
&nbsp;&nbsp;contains Direction direction, the direction to slide the row or column \
&nbsp;&nbsp;contains int index, the index of the row or column to slide \
&nbsp;&nbsp;contains int rotations, the number of times to rotate the spare tile


Neither of these views have any methods other than the getters for their internal state, as they are read-only.

We have also designed the following interface for a player to act within the game:

**Deciding to pass:** \
() -> boolean

Given that it is a player's turn (no input): \
Produce a boolean which is true if the Player does not want to act (decides to pass) or false otherwise.
Before producing these results, the player will be able to retrieve any of the following information from the context as
its strategy requires:
- Board as TileView[][] or any given Tile as a TileView
- Spare tile as TileView
- Location of any player
- Location of any player's home
- Location of player's goal tile

This will be called at the start of a player's turn. It takes no input as the player's strategy can use any
subset of the available state to make this decision, including no data, so it will all be optionally available from the context.

**Sliding and inserting:** \
BoardView, TileView, Position -> SlideAndInsertAction

Given a BoardView representation of the board, a TileView spare tile, and a Position of the player's piece: \
Produce a direction (row/column) and the index of the row or column to slide, as well as the number of times to rotate
the spare tile; or passes if no action is desired. \
Before producing these results, the player will be able to retrieve any of the following information from the context as
its strategy requires: 
- Location of any player
- Location of any player's home
- Location of player's goal tile

This will be called after the player decides whether to pass.

**Moving:** \
Position, List<Position> -> Position

Given a Position of the player's piece and a list of Positions which can be reached: \
Produce the Position of the tile to move to. \
Before producing these results, the player will be able to retrieve any of the following information from the context as
its strategy requires: 
- Board as TileView[][] or any given Tile as a TileView
- Spare tile as TileView
- Location of any other player
- Location of any player's home
- Location of player's goal tile

This will be called after sliding and inserting.