package IntegrationTests;

import model.state.IState;
import protocol.serialization.JsonSerializer;
import model.model.Position;
import player.IStrategy;
import player.Turn;

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
            IState game = mazeParser.getGame();
            mazeParser.readNext();
            Position goal = mazeParser.getCoordinate();

            game.getPlayerList().set(0, mazeParser.createPlayerWithUpdatedGoal(game.getActivePlayer(),
                    goal));

            Optional<Turn> turnPlan = strategy.createTurnPlan(,
                new ExperimentationBoardProjection(game.getBoard().getExperimentationBoard()),
                goal);
            System.out.println(mazeSerializer.turnPlanToJson(turnPlan));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
