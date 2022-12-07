package observer.Controller;

import model.state.StateProjection;

/**
 * Represents a generic neutral Observer that can step through the turns of a game.
 */
public interface IObserver {

    /**
     * Updates the observer with the given state.
     */
    void update(StateProjection state);

    /**
     * Informs the observer that the game is over and is no longer accepting updates.
     */
    void gameOver();

}
