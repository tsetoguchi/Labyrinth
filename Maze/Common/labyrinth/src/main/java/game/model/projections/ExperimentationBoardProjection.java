package game.model.projections;

import game.model.ExperimentationBoard;
import game.model.Direction;
import game.model.Position;
import game.model.Tile;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A projection of ExperimentationBoard that provides access to the necessary methods to preview slide and inserts.
 */
public class ExperimentationBoardProjection extends ReadOnlyBoardProjection {
    ExperimentationBoard experimentationBoard;

    public ExperimentationBoardProjection(ExperimentationBoard experimentationBoard) {
        super(experimentationBoard);
        this.experimentationBoard = experimentationBoard;
    }

    public boolean isValidSlideAndInsert(Direction direction, int index, int rotations) {
        return experimentationBoard.getRules().isValidSlideAndInsert(direction, index, rotations);
    }

    public Set<Position> findReachableTilePositionsAfterSlideAndInsert(
            Direction direction, int index, int rotations, Position position) {
        return experimentationBoard.findReachableTilePositionsAfterSlideAndInsert(direction, index, rotations, position);
    }

    public Optional<Position> findFirstTileMatching(Predicate<Tile> searchFunction) {
        return experimentationBoard.getFirstTileMatching(searchFunction);
    }
}
