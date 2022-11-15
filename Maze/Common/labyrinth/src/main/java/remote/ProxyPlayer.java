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
 * The communication interface for a referee to communicate with a player through a network.
 * The functions will be responsible for taking in a model data representation, serializing it into JSON,
 * sending and receiving new JSON, and deserialize the new JSON back into a model data representation.
 */
public class ProxyPlayer implements RefereePlayerInterface {

    private final Socket socket;
    private String playerName;

    public ProxyPlayer(int portNumber) {
        this.socket = new Socket();
    }

    @Override
    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {

        String gameJson = new MazeJsonSerializer(game);

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
}
