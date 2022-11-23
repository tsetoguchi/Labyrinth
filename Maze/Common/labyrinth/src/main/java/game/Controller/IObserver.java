package game.Controller;

import game.model.projections.ObserverGameProjection;

/**
 * Represents a generic neutral Observer that can step through the turns of a game.
 */
public interface IObserver {

    /**
     * Updates the observer with the given state.
     */
    void update(ObserverGameProjection state);

    /**
     * Informs the observer that the game is over and is no longer accepting updates.
     */
    void gameOver();

}
