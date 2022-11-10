package referee.clients;

import game.model.GameStatus;
import game.model.Position;
import game.model.projections.PlayerGameProjection;
import player.TurnPlan;
import referee.PlayerResult;

import java.util.Optional;

//TODO: documentation and exceptions for bad players

/**
 * A remote proxy for the player
 */
public interface PlayerClient {



    Optional<TurnPlan> takeTurn(PlayerGameProjection game);

    void returnHome(Position homeTile);

    void informGameEnd(GameStatus status, PlayerResult result);

    String getPlayerName();

    /**
     * Accepts the current game state and the current goal and updates accordingly. returning True if the player
     * successfully responds to setup.
     */
    boolean updateGoal(Position goal);
}
