package model.state;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import model.Position;

/**
 * Represents a single Player in the game.
 */
public class PlayerAvatar {

  private final Color color;
  private final List<Position> pastGoals;
  private Position goal;
  private final Position home;
  private Position currentPosition;

  /**
   * Creates a new Player with all the given properties. Used for running games.
   */
  public PlayerAvatar(Color color, Position goalPosition, Position homePosition,
      Position currentPosition) {
    this.color = color;
    this.goal = goalPosition;
    this.home = homePosition;
    this.currentPosition = currentPosition;
    this.pastGoals = new ArrayList<>();
  }

  /**
   * Creates a new Player with the given goal Treasure and Position for their home tile, and sets
   * the player's initial Position to the home tile's Position. Used for new games.
   */
  public PlayerAvatar(Color color, Position goalPosition, Position homePosition) {
    this(color, goalPosition, homePosition, homePosition);
  }

  public Position getGoal() {
    return this.goal;
  }

  public Position getHome() {
    return this.home;
  }

  public Color getColor() {
    return this.color;
  }

  public Position getCurrentPosition() {
    return this.currentPosition;
  }

  public int howManyGoalsReached() {
    return this.pastGoals.size();
  }

  public void setCurrentPosition(Position currentPosition) {
    this.currentPosition = currentPosition;
  }

  public void newGoal(Position nextGoal) {
    this.pastGoals.add(this.goal);
    this.goal = nextGoal;
  }


  public PlayerAvatar deepCopy() {
    return new PlayerAvatar(this.color, this.goal, this.home, this.currentPosition);
  }

  @Override
  public String toString() {
    return "Player{" +
        "goalPosition=" + this.goal +
        ", homePosition=" + this.home +
        ", currentAvatarPosition=" + this.currentPosition +
        '}';
  }
}
