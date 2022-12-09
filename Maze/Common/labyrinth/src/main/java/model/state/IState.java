package model.state;

import java.util.List;
import java.util.Optional;

import model.board.IBoard;
import referee.Move;

/**
 * Represents a game state with a full set of functionality that is needed to manage a State. This
 * is mainly to be given to the Referee only.
 */
public interface IState {

  /**
   * Kick the given player
   */
  void kickPlayer(PlayerAvatar player);

  /**
   * skip the current turn
   */
  void skipTurn();

  /**
   * Apply the given move
   */
  void executeTurn(Move move);

  /**
   * Determine whether the game is over
   */
  boolean isGameOver();

  /*
  Getters:
   */

  GameStatus getGameStatus();


  /**
   * Retrieve the board in the state
   */
  IBoard getBoard();

  /**
   * Return a copy of the list of players
   */
  List<PlayerAvatar> getPlayerList();

  /**
   * Returns a state projection of this state
   */
  StateProjection getStateProjection();

  Optional<SlideAndInsertRecord> getPreviousSlideAndInsert();

  PlayerAvatar getActivePlayer();

  int getBoardWidth();

  int getBoardHeight();


}
