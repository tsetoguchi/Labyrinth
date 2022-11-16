package remote;

import game.model.GameStatus;
import game.model.Position;
import game.model.projections.PlayerGameProjection;
import player.TurnPlan;
import protocol.serialization.MazeJsonParser;
import protocol.serialization.MazeJsonSerializer;
import referee.PlayerResult;
import referee.clients.RefereePlayerInterface;

import java.net.Socket;
import java.util.Optional;

/**
 * The communication interface for a referee to communicate with a player through a network. The
 * functions will be responsible for taking in a model data representation, serializing it into
 * JSON, sending and receiving new JSON, and deserialize the new JSON back into a model data
 * representation.
 */
public class ProxyPlayer implements RefereePlayerInterface {

  private final Socket client;
  private String playerName;
  private final MazeJsonSerializer serializer;




  public ProxyPlayer(Socket client, String playerName) {
    System.out.println("Proxy player: " + playerName);
    this.client = client;
    this.playerName = playerName;
    this.serializer = new MazeJsonSerializer();
  }

  @Override
  public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {

    return Optional.empty();
  }

  @Override
  public boolean setup(PlayerGameProjection game, Position goal) {
    return false;
  }

  @Override
  public boolean win(boolean playerWon) {
    return false;
  }

  @Override
  public void returnHome(Position homeTile) {

  }

  @Override
  public void informGameEnd(GameStatus status, PlayerResult result) {

  }

  @Override
  public String getPlayerName() {
    return null;
  }

  @Override
  public boolean updateGoal(Position goal) {
    return false;
  }

  public Socket getSocket() {
    return this.client;
  }
}
