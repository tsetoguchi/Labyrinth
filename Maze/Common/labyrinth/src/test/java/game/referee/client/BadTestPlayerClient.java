package game.referee.client;

import game.model.Direction;
import game.model.Position;
import game.model.projections.PlayerGameProjection;
import player.TurnPlan;

import java.util.Optional;

/**
 * A test player client that simulates a player which always cheats.
 */
public class BadTestPlayerClient extends TestPlayerClient {
    @Override
    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {
        return Optional.of(new TurnPlan(Direction.UP, 1, 0, new Position(8, 9)));
    }
}