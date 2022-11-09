package game.model;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction opposite(Direction direction) {
        if (direction == UP) {
            return DOWN;
        }
        if (direction == DOWN) {
            return UP;
        }
        if (direction == LEFT) {
            return RIGHT;
        }
        if (direction == RIGHT) {
            return LEFT;
        }
        throw new IllegalArgumentException("Direction was not an expected direction");
    }
}
