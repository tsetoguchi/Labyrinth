package model.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

/**
 * Represents the results of the game
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
