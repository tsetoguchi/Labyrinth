package player;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.List;
import json.JsonDeserializer;
import json.JsonSerializer;
import model.Position;
import model.board.Board;
import model.board.Direction;
import model.state.PlayerAvatar;
import model.state.State;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import referee.ITurn;
import referee.Move;

class AbstractStrategyTest {

  EuclideanStrategy euclid;

  PlayerAvatar p1;
  PlayerAvatar p2;
  List<PlayerAvatar> players;

  JSONObject jsonBoard;
  JSONObject jsonSpare;
  Board board;
  State state;

  @BeforeEach
  void setUp() throws JSONException {
    this.euclid = new EuclideanStrategy();
    this.p1 = new PlayerAvatar(JsonDeserializer.stringToColor("AAFFCC"),
        new Position(3, 3));
    this.p2 = new PlayerAvatar(Color.YELLOW, new Position(5, 5));
    this.players = List.of(this.p1, this.p2);

    this.jsonBoard = new JSONObject(

            "{\"connectors\":"
            + "[[\"┐\",\"─\",\"└\",\"│\",\"─\",\"┐\",\"└\"]"
            + ",[\"│\",\"┼\",\"│\",\"│\",\"┌\",\"┘\",\"┬\"]"
            + ",[\"┐\",\"─\",\"┌\",\"│\",\"├\",\"┴\",\"┤\"]"
            + ",[\"─\",\"─\",\"─\",\"│\",\"┼\",\"│\",\"─\"]"
            + ",[\"┐\",\"└\",\"┌\",\"┘\",\"┬\",\"├\",\"┴\"]"
            + ",[\"┤\",\"┼\",\"│\",\"─\",\"┐\",\"└\",\"┌\"]"
            + ",[\"┘\",\"┬\",\"├\",\"┴\",\"┤\",\"┼\",\"│\"]],"
            + "\"treasures\":[[[\"sphalerite\",\"tigers-eye\"],"
            + "[\"sphalerite\",\"tanzanite-trillion\"],"
            + "[\"sphalerite\",\"super-seven\"],"
            + "[\"purple-cabochon\",\"tanzanite-trillion\"],"
            + "[\"purple-cabochon\",\"star-cabochon\"],"
            + "[\"purple-cabochon\",\"spinel\"],"
            + "[\"purple-cabochon\",\"sphalerite\"]],"
            + "[[\"sphalerite\",\"sunstone\"],"
            + "[\"sphalerite\",\"stilbite\"],"
            + "[\"sphalerite\",\"star-cabochon\"],"
            + "[\"purple-cabochon\",\"super-seven\"],"
            + "[\"purple-cabochon\",\"ruby\"],"
            + "[\"purple-cabochon\",\"ruby-diamond-profile\"],"
            + "[\"purple-cabochon\",\"rose-quartz\"]],"
            + "[[\"sphalerite\",\"spinel\"],"
            + "[\"sphalerite\",\"sphalerite\"],"
            + "[\"ruby\",\"zoisite\"],"
            + "[\"purple-cabochon\",\"sunstone\"],"
            + "[\"purple-cabochon\",\"rock-quartz\"],"
            + "[\"purple-cabochon\",\"rhodonite\"],"
            + "[\"purple-cabochon\",\"red-spinel-square-emerald-cut\"]],"
            + "[[\"purple-cabochon\",\"tourmaline\"],"
            + "[\"purple-cabochon\",\"tourmaline-laser-cut\"],"
            + "[\"purple-cabochon\",\"tigers-eye\"],"
            + "[\"purple-cabochon\",\"stilbite\"],"
            + "[\"purple-cabochon\",\"red-diamond\"],"
            + "[\"purple-cabochon\",\"raw-citrine\"],"
            + "[\"purple-cabochon\",\"raw-beryl\"]],"
            + "[[\"purple-cabochon\",\"purple-square-cushion\"],"
            + "[\"purple-cabochon\",\"purple-spinel-trillion\"],"
            + "[\"purple-cabochon\",\"purple-oval\"],"
            + "[\"purple-cabochon\",\"purple-cabochon\"],"
            + "[\"prehnite\",\"zoisite\"],"
            + "[\"prehnite\",\"zircon\"],"
            + "[\"prehnite\",\"yellow-jasper\"]],"
            + "[[\"prehnite\",\"yellow-heart\"],"
            + "[\"prehnite\",\"yellow-beryl-oval\"],"
            + "[\"prehnite\",\"yellow-baguette\"],"
            + "[\"prehnite\",\"white-square\"],"
            + "[\"prehnite\",\"unakite\"],"
            + "[\"prehnite\",\"tourmaline\"],"
            + "[\"prehnite\",\"tourmaline-laser-cut\"]],"
            + "[[\"prehnite\",\"tigers-eye\"],"
            + "[\"prehnite\",\"tanzanite-trillion\"],"
            + "[\"prehnite\",\"super-seven\"],"
            + "[\"prehnite\",\"sunstone\"],"
            + "[\"prehnite\",\"stilbite\"],"
            + "[\"prehnite\",\"star-cabochon\"],"
            + "[\"prehnite\",\"spinel\"]]]}");

    this.jsonSpare = new JSONObject(
        "{\"1-image\":\"prehnite\",\"2-image\":\"rhodonite\",\"tilekey\":\"┤\"}"
    );

    this.board = (Board) JsonDeserializer.board(this.jsonBoard, this.jsonSpare);
    this.state = new State(this.board, this.players);
  }

  @Test
  void getCandidatesInOrder() {
  }

  @Test
  void createTurn() {
    ITurn turn = this.euclid.createTurn(this.state.getStateProjection(), new Position(5, 3));
    assertEquals(new Move(Direction.LEFT, 0, 4, new Position(5, 3)), turn);
  }
}