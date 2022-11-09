<p style="text-align: center;">Business Memorandum</p>

TO: Ben Lerner

FROM: Kyle Koo and Duncan Vogel

DATE: 10/20/2022

SUBJECT: Design for Player-Referee Protocol

As we begin to implement the players, we need to have a player-referee protocol
to allow players to interact with the game. We have designed the protocol as follows:

### Protocol Happy Path

At the start of the game (after registration), the referee sends a StartOfGame call
to every player in the game, which contains the starting board and the positions of all
players, as well as the target gems.

At any time as desired for strategy, the player may make the following API calls, but is
not required to:

Player: GetBoard \
⤷Referee: BoardView (a read-only board)

Player: GetSpareTile \
⤷Referee: TileView (a read-only tile)

Player: GetPlayers \
⤷Referee: List<PublicPlayerView (name, Position (avatar), Position (home))>

Player: GetGoalTile \
⤷Referee: Position


At the start of a player's turn, the player and referee will perform the following
series of API calls, initiated by the referee:

Referee: StartOfTurn \
&nbsp;&nbsp;&nbsp;&nbsp;(heartbeat) \
⤷Player: Ack 

Referee: RequestSlideAndInsert \
&nbsp;&nbsp;&nbsp;&nbsp;BoardView \
&nbsp;&nbsp;&nbsp;&nbsp;TileView (spare tile) \
&nbsp;&nbsp;&nbsp;&nbsp;OwnPlayerView (name, Position (avatar), Position (home), Position (goal)) \

#### If the player wishes to act this turn
⤷Player: SlideAndInsert action \
&nbsp;&nbsp;&nbsp;&nbsp;Direction \
&nbsp;&nbsp;&nbsp;&nbsp;int (index) \
&nbsp;&nbsp;&nbsp;&nbsp;int (rotations) \
⤷Referee: 200 successful slide&insert 

Referee: RequestMove \
&nbsp;&nbsp;&nbsp;&nbsp;Position (player's avatar position)
&nbsp;&nbsp;&nbsp;&nbsp;Set<Position> (reachable tiles) \
⤷Player: Move action \
&nbsp;&nbsp;&nbsp;&nbsp;Position (choice of where to move) \
⤷Referee: successful move 

Referee: TurnComplete \
&nbsp;&nbsp;&nbsp;&nbsp;(no information)

#### If the player does not wish to act this turn
⤷Player: Pass action 

Referee: TurnComplete \
&nbsp;&nbsp;&nbsp;&nbsp;(no information)

### If a player attempts to act out of order or perform an illegal action
If the player ever makes an API call which is not expected or malformed, or attempts
to take an action which the referee does not allow, the Referee will respond with
PlayerKicked, which contains a message about the reason for the removal and ceases
communications with the player.

At the end of the game, the referee sends an EndOfGame call to each non-kicked player,
which tells that player who won (it then proceeds to prize distribution).


