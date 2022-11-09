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
     * Creates a new Player with the given goal Treasure and Position for their home tile, and sets
     * the player's initial Position to the home tile's Position.
     */
    public PlayerAvatar(Color color, Position goalPosition, Position homePosition) {
        this.color = color;
        this.goalPosition = goalPosition;
        this.homePosition = homePosition;
        this.currentPosition = homePosition;
        this.hasReachedGoal = false;
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

    @Override
    public String toString() {
        return "Player{" +
                "goalPosition=" + this.goalPosition +
                ", homePosition=" + this.homePosition +
                ", currentAvatarPosition=" + this.currentPosition +
                '}';
    }
}
