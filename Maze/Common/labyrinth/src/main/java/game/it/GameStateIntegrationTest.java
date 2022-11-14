package game.it;

import game.it.processing.IntegrationTestUtils;
import game.model.PrivateGameState;
import protocol.serialization.MazeJsonParser;
import protocol.serialization.MazeJsonSerializer;
import game.model.Direction;
import game.model.Game;
import game.model.Position;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class GameStateIntegrationTest {
    private static final int BOARD_SIZE = 7;

    public static void executeTest() {
        try {
            MazeJsonParser mazeParser = new MazeJsonParser(System.in, BOARD_SIZE, BOARD_SIZE);
            MazeJsonSerializer mazeSerializer = new MazeJsonSerializer();

            mazeParser.readNext();
            PrivateGameState game = mazeParser.getGame();
            mazeParser.readNext();
            int index = mazeParser.getInt();
            mazeParser.readNext();
            Direction direction = mazeParser.getDirection();
            mazeParser.readNext();
            int rotations = mazeParser.getRotations();

            game.slideAndInsert(direction, index, rotations);
            Set<Position> reachablePositions =
                    game.getBoard().getReachablePositions(game.getActivePlayer().getCurrentPosition());
            List<Position> reachablePositionsList = IntegrationTestUtils.standardizedPositionList(reachablePositions);

            String reachablePositionsJson = mazeSerializer.reachablePositionsToJson(reachablePositionsList);

            System.out.println(reachablePositionsJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
