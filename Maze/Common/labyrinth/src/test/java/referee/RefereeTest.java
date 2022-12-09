package referee;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import json.JsonDeserializer;
import model.Position;
import model.board.Board;
import model.state.PlayerAvatar;
import model.state.State;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import player.EuclideanStrategy;
import player.IPlayer;
import player.PassStrategy;
import player.Player;
import player.RiemannStrategy;

class RefereeTest {

  PlayerAvatar p1;
  PlayerAvatar p2;
  List<PlayerAvatar> players;
  IPlayer ip1;
  IPlayer ip2;
  List<IPlayer> iPlayers;

  JSONObject jsonBoard;
  JSONObject jsonSpare;
  Board board;
  State state;

  List<Position> goals;
  Referee referee;

  @BeforeEach
  void setUp() throws JSONException {
    this.p1 = new PlayerAvatar(JsonDeserializer.stringToColor("AAFFCC"),
        new Position(3, 3));
    this.p2 = new PlayerAvatar(Color.YELLOW, new Position(5, 5));
    this.players = List.of(this.p1, this.p2);

    this.ip1 = new Player("1: Riemann", new RiemannStrategy());
    this.ip2 = new Player("2: Euclid", new EuclideanStrategy());
    this.iPlayers = new ArrayList<>(List.of(this.ip1, this.ip2));

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
    this.goals = new ArrayList<>(
        List.of(new Position(1, 1), new Position(3, 5), new Position(5, 5)));
    this.referee = new Referee(this.state, this.iPlayers, this.goals);

  }


  @Test
  void mapPlayerAvatarsToPlayerHandlers() {
    for (PlayerAvatar player : this.state.getPlayerList()) {
      assertTrue(this.referee.mapPlayerAvatarsToPlayerHandlers(this.state, this.iPlayers)
          .containsKey(player));
    }
  }

  @Test
  void runGameAllPass() {
    this.ip1 = new Player("1", new PassStrategy());
    this.ip2 = new Player("2", new PassStrategy());
    this.iPlayers = new ArrayList<>(List.of(this.ip1, this.ip2));
    this.referee = new Referee(this.state, this.iPlayers, this.goals);
    GameResults results = this.referee.runGame();
    assertEquals(new ArrayList<>(), results.getEliminated());
    assertEquals(new ArrayList<>(List.of(this.ip2.getName())), results.getWinners());
  }

  @Test
  void addObserver() {
  }
}