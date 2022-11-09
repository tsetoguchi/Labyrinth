<p style="text-align: center;">Business Memorandum</p>

TO: Ben Lerner

FROM: Kyle Koo and Duncan Vogel

DATE: 10/07/2022

SUBJECT: Data Representation Design for Labyrinth's Game State


The Referee will be defined with a Referee class. It will contain: \
A Game class, which will all elements of the game state. In the future this class will also contain all other information related to sign-ups and the communication layer. 

The Game class contains the common Board, a List of Players, an integer index indicating the current Player, and a List of Actions describing the history of the game. \
The Game class' methods are: \
A performSlide method, which takes in a Slide, updates the board appropriately, and returns void. \
An insertTile method, which takes in an Insert, inserts the loose Tile into the open slot, rotates the Tile by the amount given by the Insert, and returns void.
A movePlayer method, which takes in a Position, checks if the Position is reachable, and if so moves the current Player to that given Position. The method returns void.

The Board class contains a 2D array of Tile objects and a loose Tile object. Its methods are: \
A getTileAt method, which takes in a Position and returns the corresponding Tile. \
A getReachableTiles method, which takes in a Position and returns a List of all reachable Positions from the given Position.

The Tile class contains an up boolean, a down boolean, a left boolean, and a right boolean, indicating which directions the tile can reach. Tile also has a Treasure object. \
It has a rotate method, which takes in an integer and rotates the Tile by that many 90 degree clockwise rotations.

The Treasure class contains a Set of Gems, which is an enum.

The Player class contains a home Position, an avatar Position, as well as a goal Treasure.

The Action class contains a Player, which is the player that undertook the action. It also is implemented by three classes: \
The Slide class, which contains an Orientation, which is an enum with Row and Column as constants. It also contains an integer index, storing which row/column it's referring to, and a Direction, which is an enum with Up, Down, Left, and Right as constants. \
The Insert class, which contains an integer rotations, storing the amount of clockwise rotations that a tile would go through when it's inserted. \
The Move class, which contains a start Position and an end Position.

The Position class, which contains x and y coordinates as integers. Whenever a Position is used it should be validated that it is a valid coordinate within the board.

