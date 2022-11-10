package game.model;

import java.awt.*;

/**
 * Represents a single Player in the game.
 */
public class PlayerAvatar {
    private final Color color;
    private final Position goalPosition;
    private final Position homePosition;
    private Position currentPosition;
    private boolean hasReachedGoal;

    /**
     * Creates a new Player with all the given properties. Used for running games.
     */
    public PlayerAvatar(Color color, Position goalPosition, Position homePosition, Position currentPosition, boolean hasReachedGoal) {
        this.color = color;
        this.goalPosition = goalPosition;
        this.homePosition = homePosition;
        this.currentPosition = currentPosition;
        this.hasReachedGoal = hasReachedGoal;
    }

    /**
     * Creates a new Player with the given goal Treasure and Position for their home tile, and sets
     * the player's initial Position to the home tile's Position. Used for new games.
     */
    public PlayerAvatar(Color color, Position goalPosition, Position homePosition) {
        this(color, goalPosition, homePosition, homePosition, false);
    }

    public Position getGoalPosition() {
        return this.goalPosition;
    }

    public Position getHomePosition() {
        return this.homePosition;
    }

    public Color getColor() {
        return this.color;
    }

    public Position getCurrentPosition() {
        return this.currentPosition;
    }

    public boolean hasReachedGoal() {
        return this.hasReachedGoal;
    }

    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setHasReachedGoal(boolean hasReachedGoal) {
        this.hasReachedGoal = hasReachedGoal;
    }

    public PlayerAvatar deepCopy() {
        Position newPosition = new Position(this.currentPosition.getRow(), this.currentPosition.getColumn());

        return new PlayerAvatar(this.color, this.goalPosition, this.homePosition, newPosition, this.hasReachedGoal);
    }

    @Override
    public String toString() {
        return "Player{" +
                "goalPosition=" + this.goalPosition +
                ", homePosition=" + this.homePosition +
                ", currentAvatarPosition=" + this.currentPosition +
                '}';
    }
}
