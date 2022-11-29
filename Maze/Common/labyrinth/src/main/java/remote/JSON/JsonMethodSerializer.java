package remote.JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.Position;
import game.model.projections.PlayerStateProjection;

import java.util.Optional;

public class JsonMethodSerializer {

  private final ObjectMapper mapper;

  public JsonMethodSerializer() {
    this.mapper = new ObjectMapper();
  }

  public String generateWinJson(boolean win) throws JsonProcessingException {
    return this.mapper.writeValueAsString(new JsonWin(new boolean[] {win}));
  }

  public String generateTakeTurnJson(PlayerStateProjection game) throws JsonProcessingException {
    JsonState jsonState = JsonUtil.getJsonState(game);
    return this.mapper.writeValueAsString(new JsonTakeTurn[]{new JsonTakeTurn(jsonState)});
  }

  public String generateSetupJson(Optional<PlayerStateProjection> game, Position goal)
      throws JsonProcessingException {

    PlayerStateProjection gameState = null;

    if (game.isPresent()) {
      gameState = game.get();
      JsonState jsonState = JsonUtil.getJsonState(gameState);
      return this.mapper.writeValueAsString(new JsonSetup[]{new JsonSetup(jsonState, goal)});
    } else {
      Object[] output = new Object[]{
          "setup",
          false,
          goal
      };
      return this.mapper.writeValueAsString(output);
    }
  }
}
