package referee;

import observer.Controller.IObserver;

/**
 * An interface to run the game of labyrinth. The Referee is responsible for carrying out player
 * turns and regulating the game.
 */
public interface IReferee {

  /**
   * Runs a game of Labyrinth to completion. Returns the results of the game.
   */
  GameResults runGame();

  /**
   * Adds an observer to the referee.
   */
  void addObserver(IObserver observer);

}