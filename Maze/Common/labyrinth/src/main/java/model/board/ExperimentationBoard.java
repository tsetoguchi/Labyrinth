package model.board;

import static model.board.Direction.DOWN;
import static model.board.Direction.LEFT;
import static model.board.Direction.RIGHT;
import static model.board.Direction.UP;

import java.util.Set;

import model.Position;
import referee.Turn;

/**
 * A deep copy of the Board that can be experimented with.
 */
public class ExperimentationBoard extends Board {

    public ExperimentationBoard(int width, int height, Tile[][] tileGrid, Tile spareTile,
                                IRules rules) {
        super(width, height, tileGrid, spareTile);
    }

    public ExperimentationBoard(int width, int height) {
        super(width, height);
    }


    /**
     * Find all the Tiles that would be reachable after a given slide and insert from a given starting
     * position (on the updated board) and their corresponding positions (on the updated board)
     */

    public Set<Position> findReachableTilePositionsAfterSlideAndInsert(Turn turn, Position current) {
        Direction direction = turn.getSlideDirection();
        int index = turn.getSlideIndex();
        int rotations = turn.getSpareTileRotations();
        this.slideAndInsert(direction, index, rotations);
        Position avatarAfterSliding = this.getAvatarPositionAfterSliding(current,
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
        return originalPosition;
    }


}
