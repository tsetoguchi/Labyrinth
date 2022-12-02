package IntegrationTests.Remote;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import IntegrationTests.IntegrationUtils;
import java.util.List;

import json.JsonDeserializer;
import json.JsonSerializer;
import model.Position;
import model.state.GameResults;
import model.state.IState;
import remote.server.Server;

public class XServer {

  public static void main(String[] args) throws Exception {

    //int port = Integer.parseInt(args[0]);
    //JSONTokener jsonTokener = IntegrationUtils.getInput();
    int port = 5555;
    JSONTokener jsonTokener = new JSONTokener("{\"board\":{\"connectors\":[\n" +
            "    [\"┌\",\"┌\",\"┐\",\"│\",\"─\",\"┐\",\"└\"],\n" +
            "    [\"└\",\"─\",\"┘\",\"│\",\"┌\",\"┘\",\"┬\"],\n" +
            "    [\"─\",\"─\",\"─\",\"│\",\"├\",\"┴\",\"┤\"],\n" +
            "    [\"┼\",\"│\",\"─\",\"┐\",\"└\",\"┌\",\"┘\"],\n" +
            "    [\"┬\",\"├\",\"┴\",\"┤\",\"┼\",\"│\",\"─\"],\n" +
            "    [\"┐\",\"└\",\"┌\",\"┘\",\"┬\",\"├\",\"┴\"],\n" +
            "    [\"┤\",\"┼\",\"│\",\"─\",\"┐\",\"└\",\"┌\"]],\n" +
            "    \"treasures\":[[[\"zircon\",\"red-spinel-square-emerald-cut\"],[\"zircon\",\"red-diamond\"],[\"zircon\",\"raw-citrine\"],[\"zoisite\",\"yellow-heart\"],[\"zoisite\",\"white-square\"],[\"zoisite\",\"unakite\"],[\"zoisite\",\"tourmaline\"]],[[\"zircon\",\"raw-beryl\"],[\"zircon\",\"purple-square-cushion\"],[\"zircon\",\"purple-spinel-trillion\"],[\"zoisite\",\"yellow-beryl-oval\"],[\"zoisite\",\"tourmaline-laser-cut\"],[\"zoisite\",\"tigers-eye\"],[\"zoisite\",\"tanzanite-trillion\"]],[[\"zoisite\",\"zoisite\"],[\"zoisite\",\"zircon\"],[\"zoisite\",\"yellow-jasper\"],[\"zoisite\",\"yellow-baguette\"],[\"zoisite\",\"super-seven\"],[\"zoisite\",\"sunstone\"],[\"zoisite\",\"stilbite\"]],[[\"zoisite\",\"star-cabochon\"],[\"zoisite\",\"spinel\"],[\"zoisite\",\"sphalerite\"],[\"zoisite\",\"ruby\"],[\"zoisite\",\"ruby-diamond-profile\"],[\"zoisite\",\"rose-quartz\"],[\"zoisite\",\"rock-quartz\"]],[[\"zoisite\",\"rhodonite\"],[\"zoisite\",\"red-spinel-square-emerald-cut\"],[\"zoisite\",\"red-diamond\"],[\"zoisite\",\"raw-citrine\"],[\"zoisite\",\"raw-beryl\"],[\"zoisite\",\"purple-square-cushion\"],[\"zoisite\",\"purple-spinel-trillion\"]],[[\"zoisite\",\"purple-oval\"],[\"zoisite\",\"purple-cabochon\"],[\"zoisite\",\"prehnite\"],[\"zoisite\",\"prasiolite\"],[\"zoisite\",\"pink-spinel-cushion\"],[\"zoisite\",\"pink-round\"],[\"zoisite\",\"pink-opal\"]],[[\"zoisite\",\"pink-emerald-cut\"],[\"zoisite\",\"peridot\"],[\"zoisite\",\"padparadscha-sapphire\"],[\"zoisite\",\"padparadscha-oval\"],[\"zoisite\",\"orange-radiant\"],[\"zoisite\",\"moss-agate\"],[\"zoisite\",\"morganite-oval\"]]]},\n" +
            "    \"last\":null,\n" +
            "    \"plmt\":[\n" +
            "        {\"color\":\"blue\",\"current\":{\"column#\":1,\"row#\":0},\"goto\":{\"column#\":3,\"row#\":3},\"home\":{\"column#\":1,\"row#\":1}},\n" +
            "        {\"color\":\"red\",\"current\":{\"column#\":2,\"row#\":0},\"goto\":{\"column#\":3,\"row#\":1},\"home\":{\"column#\":5,\"row#\":3}},\n" +
            "        {\"color\":\"green\",\"current\":{\"column#\":3,\"row#\":0},\"goto\":{\"column#\":1,\"row#\":5},\"home\":{\"column#\":3,\"row#\":5}}],\n" +
            "    \"spare\":{\"1-image\":\"zircon\",\"2-image\":\"black-obsidian\",\"tilekey\":\"┤\"}}");






    JSONObject jsonGame = (JSONObject) jsonTokener.nextValue();
    IState game = JsonDeserializer.state(jsonGame);

    JSONArray jsonPlmt = jsonGame.getJSONArray("plmt");
    List<Position> goals = JsonDeserializer.goals(jsonPlmt);

    Server server = new Server(game, goals, port);
    GameResults results = server.call();
    System.out.println(JsonSerializer.gameResults(results));
  }

}
