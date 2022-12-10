package referee;

import java.util.List;

/**
 * Represents the results of the game; the winner(s) and eliminated (players who were kicked).
 */
public class GameResults {

  // the names of the winners
  List<String> winners;

  // the names of the eliminated
  List<String> eliminated;

  public GameResults(List<String> winners, List<String> eliminated) {
    this.winners = winners;
    this.eliminated = eliminated;
  }

  public List<String> getWinners() {
    return this.winners;
  }

  public List<String> getEliminated() {
    return this.eliminated;
  }
}
