package model.state;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class GameResults {

  List<String> winners;
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
