package model.state;

import java.util.List;
import java.util.Optional;

import model.board.IBoard;
import model.projections.StateProjection;
import referee.Move;

/**
 * Represents a game state with a full set of functionality that is needed to manage a State.
 * This is mainly to be given to the Referee only.
 */
public interface IState {

  void kickPlayer(PlayerAvatar player);

  void skipTurn();

  void executeTurn(Move move);

  boolean isGameOver();

  /*
  Getters:
   */

  GameStatus getGameStatus();

  IBoard getBoard();

  List<PlayerAvatar> getPlayerList();

  StateProjection getStateProjection();

  Optional<SlideAndInsertRecord> getPreviousSlideAndInsert();

  PlayerAvatar getActivePlayer();

  int getBoardWidth();

  int getBoardHeight();


}
