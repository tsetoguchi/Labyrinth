package game.exceptions;

/**
 * An exception that occurs if a game action that is not possible is attempted.
 */
public class IllegalGameActionException extends RuntimeException {

    public IllegalGameActionException(String message) {
        super(message);
    }
}
