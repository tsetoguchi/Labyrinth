package player;

import game.model.IBoard;
import game.model.Position;
import game.model.projections.PlayerGameProjection;

import java.util.Optional;


public interface IPlayer {

  Optional<Turn> takeTurn(PlayerGameProjection game);

  boolean setup(Optional<PlayerGameProjection> game, Position goal);

  boolean win(boolean playerWon);

  IBoard proposeBoard(int rows, int columns);

  String getPlayerName();

}
