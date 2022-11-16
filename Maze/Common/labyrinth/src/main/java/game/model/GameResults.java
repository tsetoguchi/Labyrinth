package game.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class GameResults {

  List<PlayerAvatar> winners;
  List<PlayerAvatar> eliminated;

  public GameResults(List<PlayerAvatar> winners, List<PlayerAvatar> eliminated) {
    this.winners = winners;
    this.eliminated = eliminated;
  }

  public List<PlayerAvatar> getWinner() {
    return this.winners;
  }

  public List<PlayerAvatar> getEliminated() {
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
