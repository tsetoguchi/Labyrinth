package IntegrationTests;

import model.Utils;
import model.state.IState;
import protocol.serialization.JsonSerializer;
import model.board.Direction;
import model.model.Position;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class GameStateIntegrationTest {

    public static void executeTest() {
        try {
            MazeJsonParser mazeParser = new MazeJsonParser(System.in);
            JsonSerializer mazeSerializer = new JsonSerializer();

            mazeParser.readNext();
            IState game = mazeParser.getGame();
            mazeParser.readNext();
            int index = mazeParser.getInt();
            mazeParser.readNext();
            Direction direction = mazeParser.getDirection();
            mazeParser.readNext();
            int rotations = mazeParser.getRotations();

            game.slideAndInsert(direction, index, rotations);
            Set<Position> reachablePositions =
                    game.getBoard().getReachablePositions(game.getActivePlayer().getCurrentPosition());
            List<Position> reachablePositionsList = Utils.standardizedPositionList(reachablePositions);

            String reachablePositionsJson = mazeSerializer.reachablePositionsToJson(reachablePositionsList);

            System.out.println(reachablePositionsJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
