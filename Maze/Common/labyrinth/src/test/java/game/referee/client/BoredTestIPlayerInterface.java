package game.referee.client;

import game.model.IBoard;
import game.model.projections.PlayerStateProjection;
import player.Turn;

import java.util.Optional;

/**
 * A test player client which simulates a player who always skips.
 */
public class BoredTestIPlayerInterface extends TestIPlayerInterface {
    @Override
    public Optional<Turn> takeTurn(PlayerStateProjection game) {
        return Optional.empty();
    }

    @Override
    public IBoard proposeBoard(int rows, int columns) {
        return null;
    }
}
