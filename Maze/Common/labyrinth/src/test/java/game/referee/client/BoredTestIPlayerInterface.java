package game.referee.client;

import game.model.projections.PlayerGameProjection;
import player.TurnPlan;

import java.util.Optional;

/**
 * A test player client which simulates a player who always skips.
 */
public class BoredTestIPlayerInterface extends TestIPlayerInterface {
    @Override
    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {
        return Optional.empty();
    }
}
