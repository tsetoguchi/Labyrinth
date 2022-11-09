package player;

import game.model.Position;
import game.model.SlideAndInsertRecord;
import game.model.projections.ExperimentationBoardProjection;
import game.model.projections.SelfPlayerProjection;

import java.util.Optional;

/**
 * Classes which implement this interface can create a TurnPlan given the information about the board and the player.
 */
public interface Strategy {
    /**
     * Given the current board, spare tile, and player information, produces a plan for the turn which includes all
     * the actions the player wishes to take, prioritizing getting to the given goal if possible.
     * Returns Optional.empty() if the player wishes to pass.
     */
    Optional<TurnPlan> createTurnPlan(ExperimentationBoardProjection board, SelfPlayerProjection playerInformation,
                                      Optional<SlideAndInsertRecord> previousSlide, Position goal);
}
