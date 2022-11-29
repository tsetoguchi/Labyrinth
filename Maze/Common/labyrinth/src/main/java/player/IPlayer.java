package player;

import game.model.IBoard;
import game.model.Position;
import game.model.projections.PlayerStateProjection;

import java.util.Optional;


public interface IPlayer {

  Optional<Turn> takeTurn(PlayerStateProjection game);

  boolean setup(Optional<PlayerStateProjection> game, Position goal);

  boolean win(boolean playerWon);

  IBoard proposeBoard(int rows, int columns);

  String getPlayerName();

}
