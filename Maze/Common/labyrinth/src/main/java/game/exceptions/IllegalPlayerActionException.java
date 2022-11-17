package game.exceptions;


/**
 * An exception that occurs if a player attempts an illegal action.
 */
public class IllegalPlayerActionException extends RuntimeException {

  public IllegalPlayerActionException(String message) {
    super(message);
  }

}
