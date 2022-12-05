package model.board;

import static model.board.Direction.DOWN;
import static model.board.Direction.LEFT;
import static model.board.Direction.RIGHT;
import static model.board.Direction.UP;

import java.util.Set;

import model.Position;
import referee.Move;

/**
 * A deep copy of the Board that can be experimented with.
 */
public class ExperimentationBoard extends Board {

    public ExperimentationBoard(int width, int height, Tile[][] tileGrid, Tile spareTile) {
        super(width, height, tileGrid, spareTile);
    }

    /**
     * Find all the Tiles that would be reachable after a given slide and insert from a given starting
     * position (on the updated board) and their corresponding positions (on the updated board)
     */
    public Set<Position> findReachableTilePositionsAfterSlideAndInsert(Move move, Position current) {
        ExperimentationBoard copy = this.getExperimentationBoard();
        Direction direction = move.getSlideDirection();
        int index = move.getSlideIndex();
        int rotations = move.getSpareTileRotations();
        copy.slideAndInsert(direction, index, rotations);
        Position avatarAfterSliding = copy.getAvatarPositionAfterSliding(current,
                copy.getWidth(), copy.getHeight(), direction, index);
        Set<Position> reachablePositions = copy.getReachablePositions(avatarAfterSliding);
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
        return originalPosition;
    }


}
