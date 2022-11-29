package player;

import model.board.IBoard;
import model.Position;
import model.projections.StateProjection;

import java.util.Optional;


public interface IPlayer {

  Optional<Turn> takeTurn(StateProjection game);

  boolean setup(Optional<StateProjection> game, Position goal);

  boolean win(boolean playerWon);

  IBoard proposeBoard(int rows, int columns);

  String getPlayerName();

}
