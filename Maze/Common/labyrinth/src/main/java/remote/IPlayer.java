//package player;
//
//import game.board.IBoard;
//import game.Position;
//import game.projections.PlayerGameProjection;
//
//import java.util.Optional;
//
///**
// * A single player in the game of Labyrinth. Represents the controls of an actual player to interact
// * with any Referee.
// */
//public interface IPlayer {
//
//  boolean win(boolean w);
//
//  boolean setup(Optional<PlayerGameProjection> state, Position goal);
//
//  /**
//   * Propose a board layout for the game.
//   **/
//  IBoard proposeBoard(int rows, int columns);
//
//  /**
//   * Given a view of the current game and a target tile to try to reach first, create a plan for the
//   * turn.
//   **/
//  Optional<Turn> takeTurn(PlayerGameProjection game);
//
//  String getName();
//
//  /**
//   * Accept a new goal from the referee. Returns true if update was successful and false otherwise.
//   */
//  boolean updateGoal(Position goal);
//}
