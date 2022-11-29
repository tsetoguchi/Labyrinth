package player;

import game.model.*;
import game.model.projections.ExperimentationBoardProjection;
import game.model.projections.PublicPlayerAvatar;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static game.model.Direction.*;

/**
 * A category of strategies which search every possible move to find a way to reach a candidate
 * goal, iterating through candidate goals until a reachable goal is found.
 */
public abstract class AbstractStrategy implements IStrategy {

  /**
   * Gets all candidates to be considered in the desired order.
   */
  abstract protected List<Position> getCandidatesInOrder(IBoard board,
                                                         Tile spareTile,
                                                         PublicPlayerAvatar playerInformation, Position goal);

  /**
   * Given the current board, spare tile, and player information, produces a plan for the turn which
   * includes all the actions the player wishes to take, prioritizing getting to the given goal if
   * possible. Returns Optional.empty() if the player wishes to pass.
   */
  @Override
  public Optional<Turn> createTurnPlan(IBoard board,
                                       PublicPlayerAvatar playerInformation,
                                       Optional<SlideAndInsertRecord> previousSlide, Position goal) {
    for (Position candidate : this.getCandidatesInOrder(board, board.getSpareTile(),
        playerInformation, goal)) {
      Optional<Turn> planForCandidate =
          this.createTurnPlanForCandidate(playerInformation.getAvatarPosition(), board, candidate,
              previousSlide);
      if (planForCandidate.isPresent()) {
        return planForCandidate;
      }
    }
    return Optional.empty();
  }

  /**
   * Returns a Turn which will result in reaching the given candidate Tile, or returns
   * Optional.empty() if it cannot find such a plan.
   */
  private Optional<Turn> createTurnPlanForCandidate(Position currentPosition,
                                                    IBoard board, Position candidate,
                                                    Optional<SlideAndInsertRecord> previousSlide) {
    int boardHeight = board.getHeight();
    int boardWidth = board.getWidth();

    for (int rowIndex = 0; rowIndex < boardHeight; rowIndex += 2) { // only check even rows
      for (int rotations = 4; rotations >= 1; rotations--) {
        for (Direction direction : new Direction[]{LEFT, RIGHT}) {
          Optional<Turn> leftOrRightPlan =
              this.trySlide(board, direction,
                  rowIndex, rotations, currentPosition, candidate, previousSlide);
          if (leftOrRightPlan.isPresent()) {
            return leftOrRightPlan;
          }
        }
      }
    }
    for (int colIndex = 0; colIndex < boardWidth; colIndex += 2) {
      for (int rotations = 4; rotations >= 1; rotations--) {
        for (Direction direction : new Direction[]{UP, DOWN}) {
          Optional<Turn> upOrDownPlan =
              this.trySlide(board, direction,
                  colIndex, rotations, currentPosition, candidate, previousSlide);
          if (upOrDownPlan.isPresent()) {
            return upOrDownPlan;
          }
        }
      }
    }

    return Optional.empty();
  }

  private Optional<Turn> trySlide(IBoard board,
                                  Direction direction, int index, int rotations,
                                  Position currentPosition,
                                  Position candidate,
                                  Optional<SlideAndInsertRecord> previousSlide) {

    boolean slideValid = board.getRules().isValidSlideAndInsert(direction, index, rotations);
    boolean reversesPrevious =
        previousSlide.isPresent() && this.reversesPreviousSlide(direction, index,
            previousSlide.get());

    if (slideValid && !reversesPrevious) {
      Set<Position> reachableTilePositionsAfterSlide =
          board.findReachableTilePositionsAfterSlideAndInsert(direction, index, rotations,
              currentPosition);
      if (reachableTilePositionsAfterSlide.contains(candidate)) {
        return Optional.of(new Turn(direction, index, rotations, candidate));
      }
    }
    return Optional.empty();
  }

  private boolean reversesPreviousSlide(Direction direction, int index,
      SlideAndInsertRecord previousSlide) {
    return index == previousSlide.getIndex()
        && direction.equals(Direction.opposite(previousSlide.getDirection()));
  }
}
