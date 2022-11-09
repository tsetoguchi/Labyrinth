<p style="text-align: center;">Business Memorandum</p>

TO: Ben Lerner

FROM: Kyle Koo and Duncan Vogel

DATE: 11/04/2022

SUBJECT: Remote protocol for Labyrinth

We are moving into playing the game with multiple players, so we now need to expand our
player-referee protocol to include signup and launching the game. 

When the referee wants to begin a game, it will open a port to API calls. Whatever game
manager is responsible for the game will inform potential players of the IP and port address, and
then the following protocol will be followed:

The referee will wait for a certain number of minutes for signup - this can be up to the
referee. The players will make a `Signup(String name)` API call to the referee during this time,
and the referee will add players to the game in the order it receives requests, assigning colors
at random. It will not accept other API calls at this time.

After the signup period has elapsed, the referee will send a `ProposeBoard(nat N, nat N)` API call
to each of the players, wait until it has received all results, then choose as starting board
and assign home and goal positions however it chooses.

It will then send `setup(GameProjection initialBoard, Position goal)` to each player to inform them
of the initial start state, as well as initializing any observers it has been configured with.

At this point, the game will proceed according to the process we outlined in player-protocol.md,
except that we will send `GameProjection` when requesting a move, which contains the information
we previously sent in as several arguments.

Once the game is complete, and `EndOfGame(GameStatus status, PlayerResult result)` has been sent to
each player, each connection is terminated.

We leave considerations of prize money or registration fees for later.

JSON data definitions:

A `GameProjection` is:
``
{
    "tileGrid": Tile[][],
    "spare": Tile,
    "self": SelfPlayer,
    "players": PublicPlayer[],
    "previousSlide": SlideAndInsert
}
``

A `Tile` is:
``
{
    "pathways": Direction[],
    "gems": String[]
}
``

A `Direction` is one of:
"UP", "DOWN", "LEFT", "RIGHT"

A  `SelfPlayer` is:
``
{
    "avatar": Position,
    "home": Position,
    "goal": Position,
    "hasReachedGoal": boolean
}
``

A `PublicPlayer` is: 
``
{
    "avatar": Position,
    "home": Position
}
``

A `SlideAndInsert` is:
``
{
    "direction": Direction,
    "index": nat,
    "rotations": nat
}
``

A `Position` is:
[nat row, nat column], where row and column are within the board dimensions

A `GameStatus` is one of:
"IN_PROGRESS",
"TREASURE_RETURNED",
"ALL_SKIPPED",
"ROUND_LIMIT_REACHED"

A `PlayerResult` is one of:
"WINNER", 
"LOSER"