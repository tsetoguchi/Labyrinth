package player;

import game.model.Board;
import game.model.Game;
import game.model.Position;
import game.model.projections.PlayerGameProjection;

import java.util.Optional;

/**
 * A single player in the game of Labyrinth.
 */
public interface Player {

    TurnPlan takeTurn(Game state);

    Object win(boolean w);

    Object setup(Optional<PlayerGameProjection> state, Position goal);

    /** Propose a board layout for the game. **/
    Board proposeBoard();
    /**
     * Given a view of the current game and a target tile to try to reach first, create a plan for the turn.
     **/
    Optional<TurnPlan> createTurnPlan(PlayerGameProjection game);

    String getName();

    /**
     * Accept a new goal from the referee. Returns true if update was successful and false otherwise.
     */
    boolean updateGoal(Position goal);
}
