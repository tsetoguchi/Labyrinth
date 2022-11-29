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

  public List<String> getWinner() {
    return this.winners;
  }

  public List<String> getEliminated() {
    return this.eliminated;
  }



  public String resultsJson() throws JsonProcessingException {

    Object[] output = new Object[]{
        this.winners,
        this.eliminated
    };
    return new ObjectMapper().writeValueAsString(output);
  }
  
}
