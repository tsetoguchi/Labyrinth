package model.state;

import java.awt.*;

import java.util.Objects;
import model.Position;

/**
 * Represents a single Player in the game.
 */
public class PlayerAvatar {

  private final Color color;
  private final Position home;
  private Position currentPosition;

  /**
   * Creates a new Player with all the given properties. Used for running games.
   */
  public PlayerAvatar(Color color, Position homePosition, Position currentPosition) {
    this.color = color;
    this.home = homePosition;
    this.currentPosition = currentPosition;
  }

  /**
   * Creates a new Player with the given goal Treasure and Position for their home tile, and sets
   * the player's initial Position to the home tile's Position. Used for new games.
   */
  public PlayerAvatar(Color color, Position homePosition) {
    this(color, homePosition, homePosition);
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


  public void setCurrentPosition(Position currentPosition) {
    this.currentPosition = currentPosition;
  }

  public PlayerAvatar deepCopy() {
    return new PlayerAvatar(this.color, this.home, this.currentPosition);
  }

  @Override
  public String toString() {
    return "Player{" +
        ", homePosition=" + this.home +
        ", currentAvatarPosition=" + this.currentPosition +
        '}';
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlayerAvatar)) {
      return false;
    }
    PlayerAvatar that = (PlayerAvatar) o;
    return this.color.equals(that.color);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.color);
  }
}
