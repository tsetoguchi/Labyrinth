package game.IntegrationTests;

import game.model.PrivateState;
import org.json.JSONObject;
import protocol.serialization.MazeJsonParser;
import protocol.serialization.JsonSerializer;
import game.model.Position;
import game.model.projections.ExperimentationBoardProjection;
import game.model.projections.SelfPlayerProjection;
import player.IStrategy;
import player.TurnPlan;

import java.io.IOException;
import java.util.Optional;

public class StrategyIntegrationTest {
    public static void executeTest() {
        try {
            MazeJsonParser mazeParser = new MazeJsonParser(System.in);
            JsonSerializer mazeSerializer = new JsonSerializer();

            mazeParser.readNext();
            IStrategy strategy = mazeParser.getStrategy();
            mazeParser.readNext();
            PrivateState game = mazeParser.getGame();
            mazeParser.readNext();
            Position goal = mazeParser.getCoordinate();

            game.getPlayerList().set(0, mazeParser.createPlayerWithUpdatedGoal(game.getActivePlayer(),
                    goal));

            Optional<TurnPlan> turnPlan = strategy.createTurnPlan(
                    new ExperimentationBoardProjection(game.getBoard().getExperimentationBoard()),
                    new SelfPlayerProjection(game.getActivePlayer()),
                    Optional.empty(),
                    goal);
            System.out.println(mazeSerializer.turnPlanToJson(turnPlan));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
