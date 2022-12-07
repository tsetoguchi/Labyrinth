# READ ME
##Abstract:
This project is an implementation of the classic board game Labyrinth. 
It runs a server that hosts a game and allows players from across a network to play against one another.

##Main Components:

###Referee:
The purpose of the Referee is to run a game of Labyrinth, enforcing the specified rules.
It facilitates the communication between the players and the gamestate, kicking players as they misbehave.
A player misbehaves when they fail to communicate a valid turn/response in the specified time.
The Referee is the source of truth for the game and holds the accurate gamestate and list of players.
Due to this, it protects itself against the other components (with exception to the Model).

* Referee: This class carries out the game.
  * Current functionality includes running a given state with the given players to completion.
  * PlayerHandler: inner class that protects the Referee from errors and timeouts from the Players by wrapping their method calls.
* IRules: The rules of the game. Use this interface to tell if a slide/insert is valid and if a position is movable or not.
* ITurn: This interface represents a Player's turn in the game.
  * Move: Represents a slide index, slide direction, insert rotation, and move to position
  * Pass: Represents no action performed in this turn
* GoalHandler: Handles the assignment of goals as the game is played.

###Player:
The purpose of a player is to play a game of Labyrinth. Currently, the IPlayer interface has the capability to 
receive a setup state with their initial goal, receive a new goal (also using the setup method), 
respond to the referee when asked for a turn, and receive whether they've won the game at the game's completion.
Future implementation includes the ability to propose a board for the game! 
* IPlayer: current implementation of an AI player that references a given IStrategy to determine their turn.
* IStrategy: a strategy that, given a projection of the state and a destination, returns an ITurn of the move a player should carry out.

###Model:
The model holds information about the board, spare tile, and players configuration. It carries out the 
actual sliding/inserting/moving players that occurs at each turn, as well as the home and color of each player.
* IState: Has a Board and list of PlayerAvatars and performs the following functionality:
kicking a player, passing a turn, performing a move, and giving relevant information (active player, player list, if the game is over, board information)
* PlayerAvatar: Has the player's current position, home, and color.
* Board: The configuration of tiles, including the spare. Operations include performing a slide/insert and getting the reachable positions through the maze given a current one.


###Observer:
The observer offers an interactive GUI that displays consecutive states of the game until the game is finished.
It is also capable of saving the current state of the game as a JSON file.

###Remote:
The remote interaction layer allows a server to host a game with client players across a network.
* Server:
* ProxyPlayer:
* Client:
* ProxyReferee: