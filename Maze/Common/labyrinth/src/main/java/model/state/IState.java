package model.state;

import java.awt.Dimension;
import model.board.Direction;
import model.Position;
import model.board.IBoard;
import model.projections.StateProjection;
import referee.Turn;

import java.util.List;
import java.util.Optional;

/**
 * Represents a game state with a full set of functionality that is needed to manage a State.
 * This is mainly to be given to the Referee only.
 */
public interface IState {

  GameStatus getGameStatus();

  IBoard getBoard();

  List<PlayerAvatar> getPlayerList();

  StateProjection getStateProjection();

  void kickPlayer(PlayerAvatar player);

  void skipTurn();

  void executeTurn(Turn turn);

  Optional<SlideAndInsertRecord> getPreviousSlideAndInsert();

  PlayerAvatar getActivePlayer();

  int getBoardWidth();

  int getBoardHeight();


}
