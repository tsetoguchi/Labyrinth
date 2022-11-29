package game.referee.client;

import game.model.IBoard;
import game.model.projections.PlayerGameProjection;
import player.Turn;

import java.util.Optional;

/**
 * A test player client which always tries to take the same action.
 */
public class ObsessiveTestIPlayerInterface extends TestIPlayerInterface {
    private final Turn plan;

    public ObsessiveTestIPlayerInterface(Turn plan) {
        this.plan = plan;
    }

    @Override
    public Optional<Turn> takeTurn(PlayerGameProjection game) {
        return Optional.of(this.plan);
    }

    @Override
    public IBoard proposeBoard(int rows, int columns) {
        return null;
    }
}