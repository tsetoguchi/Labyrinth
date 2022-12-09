package player;

import model.board.IBoard;
import model.Position;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A AbstractStrategy that evaluates candidates in order of Euclidean distance from the goal tile.
 */
public class EuclideanStrategy extends AbstractStrategy {

  /**
   * Gets all candidates to be considered in order of Euclidean distance from the goal Tile,
   * breaking ties by row-column ordering, but always starting with the goal Tile.
   */
  @Override
  protected List<Position> getCandidatesInOrder(IBoard board,
      Position goal) {
    List<Position> candidatePositions = new ArrayList<>();
    for (int rowIndex = 0; rowIndex < board.getHeight(); rowIndex++) {
      for (int colIndex = 0; colIndex < board.getWidth(); colIndex++) {
        Position position = new Position(rowIndex, colIndex);
        if (!position.equals(goal)) {
          candidatePositions.add(position);
        }
      }
    }

    Comparator<Position> smallerEuclideanDistanceComparator = (Position p1, Position p2) -> {
      double differenceInEuclideanDistance = Position.getEuclideanDistance(p1, goal)
          - Position.getEuclideanDistance(p2, goal);
      if (differenceInEuclideanDistance < 0) {
        return -1;
      } else if (differenceInEuclideanDistance > 0) {
        return 1;
      } else {
        return new Position.RowColumnOrderComparator().compare(p1, p2);
      }
    };
    candidatePositions.sort(smallerEuclideanDistanceComparator);

    candidatePositions.add(0, goal);
    return candidatePositions;
  }
}
