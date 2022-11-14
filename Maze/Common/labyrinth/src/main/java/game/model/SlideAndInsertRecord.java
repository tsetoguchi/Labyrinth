package game.model;

/**
 * A record of the slide and insert action taken on a turn.
 */
public class SlideAndInsertRecord {
    private final Direction direction;
    private final int index;
    private final int rotations;

    public SlideAndInsertRecord(Direction direction, int index, int rotations) {
        this.direction = direction;
        this.index = index;
        this.rotations = rotations;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public int getIndex() {
        return this.index;
    }

    public int getRotations() {
        return this.rotations;
    }
}
