package model.state;

/**
 * The current state of the game, either IN_PROGRESS while running or one of a number of different
 * game over conditions.
 */
public enum GameStatus {
  IN_PROGRESS,
  ALL_SKIPPED,
  ROUND_LIMIT_REACHED,
  NO_REMAINING_PLAYERS
}
