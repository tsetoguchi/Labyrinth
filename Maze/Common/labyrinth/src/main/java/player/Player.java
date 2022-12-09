package player;

import model.Position;
import model.board.Board;
import model.board.IBoard;
import model.state.StateProjection;
import referee.ITurn;

import java.util.Optional;

/**
 * An AI player that determines its moves by asking its strategy.
 */
public class Player implements IPlayer {

  protected String name;
  protected IStrategy strategy;
  protected Position destination;
  protected Optional<Boolean> winner;

  public Player(String name, IStrategy strategy) {
    this.name = name;
    this.strategy = strategy;
    this.winner = Optional.empty();
  }

  @Override
  public boolean win(boolean w) {
    this.winner = Optional.of(w);
    return true;
  }

  @Override
  public boolean setup(Optional<StateProjection> state, Position goal) {
    this.destination = goal;
    return true;
  }

  @Override
  public ITurn takeTurn(StateProjection game) {
    return this.strategy.createTurn(game, this.destination);
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public IBoard proposeBoard(int rows, int columns) {
    return new Board(columns, rows);
  }

}
