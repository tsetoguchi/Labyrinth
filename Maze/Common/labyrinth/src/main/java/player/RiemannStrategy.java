package player;

import game.model.IBoard;
import game.model.Position;
import game.model.Tile;
import game.model.projections.ExperimentationBoardProjection;
import game.model.projections.PublicPlayerAvatar;

import java.util.ArrayList;
import java.util.List;

/**
 * A AbstractStrategy that evaluates candidates in row-column order.
 */
public class RiemannStrategy extends AbstractStrategy {

  /**
   * Gets all candidates to be considered in row-column order, but always starting with the goal
   * Tile.
   */
  @Override
  protected List<Position> getCandidatesInOrder(IBoard board,
                                                Tile spareTile,
                                                PublicPlayerAvatar playerInformation, Position goal) {
    List<Position> candidates = new ArrayList<>();
    candidates.add(goal);
    for (int rowIndex = 0; rowIndex < board.getHeight(); rowIndex++) {
      for (int colIndex = 0; colIndex < board.getWidth(); colIndex++) {
        Position candidatePosition = new Position(rowIndex, colIndex);
        if (!candidatePosition.equals(goal)) {
          candidates.add(candidatePosition);
        }
      }
    }
    return candidates;
  }
}
