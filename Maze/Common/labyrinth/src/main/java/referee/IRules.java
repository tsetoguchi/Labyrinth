package referee;


import model.Position;

/**
 * Represents the Rules for the game that must be followed for it to be played correctly
 */
public interface IRules {

  /**
   * Validate that sliding a row or column at the given index in the specified direction and
   * inserting the spare tile does not violate game rules or pass invalid arguments.
   */
  boolean isValidSlideAndInsert(Move move, int width, int height);

  /**
   * Returns true if the given position is immovable.
   */
  boolean immovablePosition(Position position);

}
