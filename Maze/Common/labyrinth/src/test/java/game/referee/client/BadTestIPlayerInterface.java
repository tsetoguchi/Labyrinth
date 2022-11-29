package model.referee.client;

import model.board.IBoard;
import model.board.Direction;
import model.model.Position;
import model.projections.StateProjection;
import player.Turn;

import java.util.Optional;

/**
 * A test player client that simulates a player which always cheats.
 */
public class BadTestIPlayerInterface extends TestIPlayerInterface {
    @Override
    public Optional<Turn> takeTurn(StateProjection game) {
        return Optional.of(new Turn(Direction.UP, 1, 0, new Position(8, 9)));
    }

    @Override
    public IBoard proposeBoard(int rows, int columns) {
        return null;
    }
}
