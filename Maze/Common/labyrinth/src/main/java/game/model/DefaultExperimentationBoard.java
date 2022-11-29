package game.model;

import static game.model.Direction.DOWN;
import static game.model.Direction.LEFT;
import static game.model.Direction.RIGHT;
import static game.model.Direction.UP;

import java.util.HashSet;
import java.util.Set;

/**
 * An ExperimentationBoard for DefaultBoard.
 */
public class DefaultExperimentationBoard extends DefaultBoard implements ExperimentationBoard {

    /**
     * Creates a DefaultExperimentationBoard with a deep copy of the given DefaultBoard's state.
     */
    public DefaultExperimentationBoard(IBoard board) {
        super(copyGrid(board), copyTile(board.getSpareTile()));
    }
//
//    static Tile[][] copyGrid(IBoard board) {
//        Tile[][] newGrid = new Tile[board.getHeight()][board.getWidth()];
//
//        for (int row = 0; row < newGrid.length; row++) {
//            for (int col = 0; col < newGrid[row].length; col++) {
//                newGrid[row][col] = copyTile(board.getTileAt(new Position(row, col)));
//            }
//        }
//        return newGrid;
//    }
//
//    private static Tile copyTile(Tile tile) {
//        Set<Direction> newDirections = new HashSet<>();
//        if (tile.connects(Direction.UP)) {newDirections.add(Direction.UP);}
//        if (tile.connects(Direction.DOWN)) {newDirections.add(Direction.DOWN);}
//        if (tile.connects(Direction.LEFT)) {newDirections.add(Direction.LEFT);}
//        if (tile.connects(Direction.RIGHT)) {newDirections.add(Direction.RIGHT);}
//
//        return new Tile(newDirections, tile.getTreasure());
//    }

    /**
     * Find all the Tiles that would be reachable after a given slide and insert from a given starting
     * position (on the updated board) and their corresponding positions (on the updated board)
     */
    @Override
    public Set<Position> findReachableTilePositionsAfterSlideAndInsert(Direction direction, int index, int rotations, Position position) {
        this.slideAndInsert(direction, index, rotations);
        Position avatarAfterSliding = this.getAvatarPositionAfterSliding(position,
                this.getWidth(), this.getHeight(), direction, index);
        Set<Position> reachablePositions = this.getReachablePositions(avatarAfterSliding);

        this.slideAndInsert(Direction.opposite(direction), index, 0);
        this.getSpareTile().rotate(4 - rotations);
        return reachablePositions;
    }

    /**
     * Gets the new position of an avatar after a slide was performed.
     */
    private Position getAvatarPositionAfterSliding(Position originalPosition, int width, int height,
                                                   Direction direction, int index) {
        if ((direction == LEFT || direction == RIGHT) && originalPosition.getRow() == index) {
            if (direction == LEFT) {
                return originalPosition.addDeltaWithBoardWrap(0, -1, height, width);
            } else {
                return originalPosition.addDeltaWithBoardWrap(0, 1, height, width);
            }
        } else if ((direction == UP || direction == DOWN) && originalPosition.getColumn() == index) {
            if (direction == UP) {
                return originalPosition.addDeltaWithBoardWrap(-1, 0, height, width);
            } else {
                return originalPosition.addDeltaWithBoardWrap(1, 0, height, width);
            }
        }
        return originalPosition; //TODO: Consider consolidating
    }
}
