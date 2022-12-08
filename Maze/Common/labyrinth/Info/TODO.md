* Server: 
  * If given a gamestate, the server should enforce that the number of remote clients and number of players in the gamestate are equal.
  * The server should require a client to send it's name in the required time before it considers it a valid connection. Currently, the server establishes as many connections as are tried and filters out players that never sent their name after.

* NetUtil
  * readInput(StringBuilder str, Socket sock): this method can not read in complex inputs. Currently, it appends whatever is in the stream to the string builder. If given one and a half JSON values, the ProxyPlayer/ProxyReferee trying to interpret this would not know what to do. To fix this in future implementations, we could either read in a single character at a time (which we thought would be inefficient with timeouts being a potential issue) or use some sort of tokener to parse through the input.

* Referee:
  * Our referee no longer has the functionality of starting a game given a list of players. Given a list of players, the referee would need to ask the players to propose a board, choose one the boards proposed, create an IState, randomly generate goals, and then play the game to completion. This was out of our scope for the final weeks and something we never got around.
  * ObserverHandler: currently our Referee is not protected from Observers. Ideally, the Referee would interact with the Observer in a similar fashion to how to communicates with Players using our PlayerHandler.
  * 

