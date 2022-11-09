package game.model;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A non-canonical snapshot of the Board on the start of a player's turn that players can use to
 * preview a slide and insert without actually performing it.
 */
public interface ExperimentationBoard extends Board {

    /**
     * Find all the Tiles that would be reachable after a given slide and insert from a given starting
     * position (on the updated board) and their corresponding positions (on the updated board)
     */
    Set<Position> findReachableTilePositionsAfterSlideAndInsert(Direction direction, int index, int rotations, Position position);

}
