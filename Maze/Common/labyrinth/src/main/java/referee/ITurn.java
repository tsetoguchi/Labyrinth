package referee;

/**
 * A turn of the game Labyrinth. Can be a Pass or a Move.
 */
public interface ITurn {

  /**
   * Returns true if the ITurn is a Move.
   */
  boolean isMove();

  /**
   * Returns the Move of the ITurn. Should only be called after isMove() confirms this is a Move.
   * Throws exception when called on a Pass object!
   */
  Move getMove();

}
