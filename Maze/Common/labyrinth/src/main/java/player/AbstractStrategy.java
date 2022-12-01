package player;

import model.Position;

import referee.DefaultRules;
import model.board.Direction;
import model.board.ExperimentationBoard;
import model.board.IBoard;
import referee.IRules;
import model.projections.StateProjection;
import model.state.PlayerAvatar;
import model.state.SlideAndInsertRecord;
import referee.ITurn;
import referee.Move;
import referee.Pass;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static model.board.Direction.*;

/**
 * A category of strategies which search every possible move to find a way to reach a candidate
 * goal, iterating through candidate goals until a reachable goal is found.
 */
public abstract class AbstractStrategy implements IStrategy {

  private final IRules rules;
  private StateProjection state;

  public AbstractStrategy(IRules rules) {
    this.rules = rules;
  }

  public AbstractStrategy() {
    this(new DefaultRules());
  }
  /**
   * Gets all candidates to be considered in the desired order.
   */
  abstract protected List<Position> getCandidatesInOrder(IBoard board, Position goal);

  /**
   * Given the current board, spare tile, and player information, produces a plan for the turn which
   * includes all the actions the player wishes to take, prioritizing getting to the given goal if
   * possible. Returns Optional.empty() if the player wishes to pass.
   * @return
   */
  @Override
  public ITurn createTurnPlan(StateProjection state, Position goal) {
    this.state = state;
    ExperimentationBoard board = this.state.getBoard();
    PlayerAvatar player = this.state.getSelf();
    ITurn planForCandidate;
    for (Position candidate : this.getCandidatesInOrder(board, goal)) {
      planForCandidate =
          this.createTurnPlanForCandidate(player.getCurrentPosition(), candidate);
      if (planForCandidate.isMove()) {
        return planForCandidate;
      }
    }
    return new Pass();
  }

  /**
   * Returns a Move which will result in reaching the given candidate Tile, or returns
   * Optional.empty() if it cannot find such a plan.
   */
  private ITurn createTurnPlanForCandidate(Position currentPosition, Position candidate){
    if(currentPosition.equals(new Position(0, 3))){
      int x = 8;
    }
    ExperimentationBoard board = this.state.getBoard();
    int boardHeight = board.getHeight();
    int boardWidth = board.getWidth();

    for (int rowIndex = 0; rowIndex < boardHeight; rowIndex++) {
      for (int rotations = 4; rotations >= 1; rotations--) {
        for (Direction direction : new Direction[]{LEFT, RIGHT}) {
          Move toTry = new Move(direction, rowIndex, rotations, candidate);
          if(this.validTurn(currentPosition, toTry)){
            return toTry;
          }
        }
      }
    }

    for (int colIndex = 0; colIndex < boardWidth; colIndex++) {
      for (int rotations = 4; rotations >= 1; rotations--) {
        for (Direction direction : new Direction[]{UP, DOWN}) {
          Move toTry = new Move(direction, colIndex, rotations, candidate);
          if(this.validTurn(currentPosition, toTry)){
            return toTry;
          }
        }
      }
    }

    return new Pass();
  }

  private boolean validTurn(Position current, Move move) {
    ExperimentationBoard board = this.state.getBoard();
    Optional<SlideAndInsertRecord> previousSlide = this.state.getPreviousSlideAndInsert();
    boolean slideValid = this.rules.isValidSlideAndInsert(move, board.getWidth(), board.getHeight());
    Position candidate = move.getMoveDestination();
    boolean reversesPrevious = previousSlide.isPresent() && previousSlide.get().revertsCheck(move);

    if (slideValid && !reversesPrevious) {
      Set<Position> reachableTilePositionsAfterSlide =
          board.findReachableTilePositionsAfterSlideAndInsert(move, current);
      return reachableTilePositionsAfterSlide.contains(candidate);
    }
    return false;
  }

}
