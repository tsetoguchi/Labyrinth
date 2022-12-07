package player;

import model.board.IBoard;
import model.Position;
import model.state.StateProjection;
import referee.ITurn;

import java.util.Optional;


public interface IPlayer {

  ITurn takeTurn(StateProjection game);

  boolean setup(Optional<StateProjection> game, Position goal);

  boolean win(boolean playerWon);

  IBoard proposeBoard(int rows, int columns);

  String getName();

}
