* Implement rendering
* Implement RefereeState serialization
* Add xgames-with-observer
* Finish design
* Add referee checks for players timing out - the referee must always have a way to take back control
* Add a constructor for referee that just takes in a list of players - starts a game from scratch
* Add any missing documentation - What is Player vs PlayerClient
* Change skip condition to be all players in a round passing
* Factor running round into helper
* Change 
* Move int tests into separate package OR into test folder, and update maven build to reflect
* Purpose statements/documentation for (really just go through everything):
  * PlayerClient, Player
  * Determining winners
* Change slidable rows to not be hardcoded - add a 'getSlidableIndices' to Board.