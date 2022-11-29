package player;

import game.model.IBoard;
import game.model.Position;
import game.model.SlideAndInsertRecord;

import game.model.projections.PublicPlayerProjection;
import java.util.Optional;

/**
 * Classes which implement this interface can create a Turn given the information about the
 * board and the player.
 */
public interface IStrategy {

  /**
   * Given the current board, spare tile, and player information, produces a plan for the turn which
   * includes all the actions the player wishes to take, prioritizing getting to the given goal if
   * possible. Returns Optional.empty() if the player wishes to pass.
   */
  Optional<Turn> createTurnPlan(IBoard board,
                                PublicPlayerProjection playerInformation,
                                Optional<SlideAndInsertRecord> previousSlide, Position goal);
}
