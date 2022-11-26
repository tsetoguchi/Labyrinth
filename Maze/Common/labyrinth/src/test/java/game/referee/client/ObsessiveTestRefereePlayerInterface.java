package game.referee.client;

import game.model.Board;
import game.model.projections.PlayerGameProjection;
import player.TurnPlan;

import java.util.Optional;

/**
 * A test player client which always tries to take the same action.
 */
public class ObsessiveTestRefereePlayerInterface extends TestIPlayer {
    private final TurnPlan plan;

    public ObsessiveTestRefereePlayerInterface(TurnPlan plan) {
        this.plan = plan;
    }

    @Override
    public Optional<TurnPlan> takeTurn(PlayerGameProjection game) {
        return Optional.of(this.plan);
    }

    @Override
    public Board proposeBoard(int rows, int columns) {
        return null;
    }
}
