package game.it;

import game.model.PrivateGameState;
import protocol.serialization.MazeJsonParser;
import protocol.serialization.MazeJsonSerializer;
import game.model.Game;
import game.model.Position;
import game.model.projections.ExperimentationBoardProjection;
import game.model.projections.SelfPlayerProjection;
import player.IStrategy;
import player.TurnPlan;

import java.io.IOException;
import java.util.Optional;

public class StrategyIntegrationTest {
    private static final int BOARD_SIZE = 7;
    public static void executeTest() {
        try {
            MazeJsonParser mazeParser = new MazeJsonParser(System.in, BOARD_SIZE, BOARD_SIZE);
            MazeJsonSerializer mazeSerializer = new MazeJsonSerializer();

            mazeParser.readNext();
            IStrategy strategy = mazeParser.getStrategy();
            mazeParser.readNext();
            PrivateGameState game = mazeParser.getGame();
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
